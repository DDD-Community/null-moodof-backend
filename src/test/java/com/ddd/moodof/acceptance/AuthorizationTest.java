package com.ddd.moodof.acceptance;

import com.ddd.moodof.domain.model.user.AuthProvider;
import com.ddd.moodof.adapter.infrastructure.security.TokenProvider;
import com.ddd.moodof.adapter.infrastructure.security.UserPrincipal;
import com.ddd.moodof.adapter.infrastructure.configuration.CookieUtils;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthorizationTest {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 3600;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private WebApplicationContext webappContext;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext)
                .apply(springSecurity())
                .build();
    }
    @Test
    @WithMockUser(username = "moodof ddd",password = "", authorities = "USER")
    public void JWT_토큰_생성() throws Exception {
        OAuth2User oAuth2User = Mockito.mock(OAuth2User.class);
        Authentication authorizationRequest = Mockito.mock(Authentication.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);
        User user = new User();
        user.setEmail("ddd.moodof@gmail.com");
        user.setNickname("moodof ddd");
        user.setProfileUrl("https://lh6.googleusercontent.com/-4vjsYgdYzVE/AAAAAAAAAAI/AAAAAAAAAAA/AMZuuckpFmWzV5cRiDCebObd-AidXzUC8g/s96-c/photo.jpg");
        user.setProvider(AuthProvider.google);
        user.setProviderId("107406868053916247309");

        userPrincipal.create(user, oAuth2User.getAttributes());
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Mockito.whens() for your authorization object
        when(securityContext.getAuthentication()).thenReturn(authorizationRequest);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userPrincipal);
        when(securityContext.getAuthentication()).thenReturn(authorizationRequest);
        System.err.println("authorizationRequest.getPrincipal() = " + authorizationRequest.getPrincipal());

        /*
        JWT 토큰 생성후 값 받아오기
         */
        tokenProvider.createToken(authorizationRequest);
        String token = tokenProvider.getToken();

        /*
        JWT 토큰처리
        */
        System.out.println("token = " + token);
        mockMvc.perform(RequestSecurityFactory.securityFactory("/user/token", token)
                .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());;

    }
    @Test
    @WithMockUser(username = "moodof ddd",password = "", authorities = "USER")
    public void 토큰_쿠키_생성() throws Exception {
        Authentication authorizationCookieRequest = Mockito.mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        if (authorizationCookieRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        }
        Cookie cookie = CookieUtils.addCookieTest(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationCookieRequest), cookieExpireSeconds);
        request.setAttribute(REDIRECT_URI_PARAM_COOKIE_NAME, cookie.getValue());
        String token = cookie.getValue();
        System.out.println("token = " + token);
        mockMvc.perform(RequestSecurityFactory.securityFactory("/user/token", token)
                .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    /**
     * 인증 토큰값으로 인증가능
     * @throws Exception
     */
    @Test
    void 토큰_인증_헤더() throws Exception {
        String token = "";
        System.err.println(token);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/token")
        .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    /**
     * 시큐리티 클라이언트 헤더값 세팅
     */
    public static class RequestSecurityFactory {
        /**
         * 요청값 헤더값 세팅
         * @param url
         * @param token
         * @return
         */
        public static MockHttpServletRequestBuilder securityFactory(String url, String token) {
            return MockMvcRequestBuilders.get(url)
                    .header(AUTHORIZATION, "Bearer " + token);
        }
    }

}