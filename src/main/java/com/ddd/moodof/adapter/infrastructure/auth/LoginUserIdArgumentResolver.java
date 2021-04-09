package com.ddd.moodof.adapter.infrastructure.auth;

import com.ddd.moodof.adapter.infrastructure.auth.exception.InvalidTokenException;
import com.ddd.moodof.adapter.presentation.LoginUserId;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class LoginUserIdArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String SP = " ";
    private static final int TOKEN_INDEX = 1;
    private static final int TYPE_INDEX = 0;
    private static final String BEARER = "bearer";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String[] authorizations = Objects.requireNonNull(webRequest.getHeader(AUTHORIZATION)).split(SP);
        String type = authorizations[TYPE_INDEX];
        // TODO: 2021/04/07 현재는 userId
        String token = authorizations[TOKEN_INDEX];

        if (!type.equalsIgnoreCase(BEARER)) {
            throw new InvalidTokenException("지원하지 않는 토큰 타입입니다. type : " + type);
        }

        Long userId = Long.parseLong(token);

        // TODO: 2021/04/07  User 구현시, UserRepository.existById(userId)

        return userId;
    }
}
