package com.ddd.moodof;

import com.ddd.moodof.adapter.infrastructure.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class MoodofApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoodofApplication.class, args);
    }

}
