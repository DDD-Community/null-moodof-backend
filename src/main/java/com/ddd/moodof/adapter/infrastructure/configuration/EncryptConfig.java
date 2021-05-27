package com.ddd.moodof.adapter.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "encrypt")
@Getter
@Setter
public class EncryptConfig {
    private String key;
}
