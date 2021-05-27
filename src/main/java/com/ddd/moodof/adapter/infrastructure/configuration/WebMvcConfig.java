package com.ddd.moodof.adapter.infrastructure.configuration;

import com.ddd.moodof.adapter.infrastructure.security.LoginUserIdArgumentResolver;
import com.ddd.moodof.adapter.infrastructure.security.SharedBoardIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final long MAX_AGE_SECS = 3600L;

    private final LoginUserIdArgumentResolver loginUserIdArgumentResolver;

    private final SharedBoardIdArgumentResolver sharedBoardIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserIdArgumentResolver);
        resolvers.add(sharedBoardIdArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}
