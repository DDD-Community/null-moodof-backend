package com.ddd.moodof.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class StoragePhotoDTO {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class Create {
        private String uri;
        private String representativeColor;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long userId;
        private String uri;
        private String representativeColor;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
    }
}
