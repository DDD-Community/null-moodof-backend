package com.ddd.moodof.adapter.infrastructure.security;

import com.ddd.moodof.adapter.infrastructure.configuration.EncryptConfig;

import com.ddd.moodof.adapter.infrastructure.security.encrypt.EncryptUtil;
import com.ddd.moodof.adapter.presentation.SharedBoardId;
import com.ddd.moodof.domain.model.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class SharedBoardIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final BoardRepository boardRepository;

    private final EncryptConfig encryptConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SharedBoardId.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        SharedBoardId customParam = parameter.getParameterAnnotation(SharedBoardId.class);
        String key = webRequest.getParameter(customParam.value());

        try {
            String id = EncryptUtil.decryptAES256(key, encryptConfig.getKey());
            Long boardId = Long.valueOf(id);
            boardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id = " + id));
            return boardId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
