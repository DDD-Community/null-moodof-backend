package com.ddd.moodof.adapter.infrastructure.security.oauth2.user;

import com.ddd.moodof.adapter.infrastructure.security.exception.OAuth2AuthenticationProcessingException;
import com.ddd.moodof.domain.model.user.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
    }
}
