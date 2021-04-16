package com.ddd.moodof.adapter.infrastructure.security;

import com.ddd.moodof.adapter.infrastructure.security.exception.InvalidTokenException;
import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class LoginUserIdArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String SP = " ";
    private static final int TOKEN_INDEX = 1;
    private static final int TYPE_INDEX = 0;
    private static final String BEARER = "bearer";

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String[] authorizations = Objects.requireNonNull(webRequest.getHeader(AUTHORIZATION)).split(SP);
        String type = authorizations[TYPE_INDEX];
        String token = authorizations[TOKEN_INDEX];

        if (!type.equalsIgnoreCase(BEARER)) {
            throw new InvalidTokenException("지원하지 않는 토큰 타입입니다. type : " + type);
        }

        Long userId = tokenProvider.getUserId(token);

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("존재하지 않는 user, userId = " + userId);
        }

        return userId;
    }
}
