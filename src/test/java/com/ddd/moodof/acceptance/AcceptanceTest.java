package com.ddd.moodof.acceptance;

import com.ddd.moodof.adapter.infrastructure.security.TokenProvider;
import com.ddd.moodof.domain.model.user.AuthProvider;
import com.ddd.moodof.domain.model.user.User;
import com.ddd.moodof.domain.model.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    private static final String BEARER = "Bearer ";

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    protected <T, U> U postWithLogin(T request, String uri, Class<U> response, Long userId) {
        try {
            String token = tokenProvider.createToken(userId);
            String body = objectMapper.writeValueAsString(request);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(body)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.matchesRegex(uri + "/\\d*")))
                    .andReturn();

            return objectMapper.readValue(result.getResponse().getContentAsString(), response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected User signUp() {
        return userRepository.save(new User(null, "test@test.com", "password", "nickname", "profileUrl", null, null, AuthProvider.google, "providerId"));
    }

    protected <T> T getWithLogin(String uri, Class<T> response, Long userId) {
        try {

            String token = tokenProvider.createToken(userId);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            return objectMapper.readValue(result.getResponse().getContentAsString(), response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }
}
