package com.ddd.moodof.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SharedDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class SharedBoardRequest{
        private Long id;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class SharedBoardResponse {
        private Long id;

        private String sharedURL;

        private String sharedKey;

        public static SharedDTO.SharedBoardResponse from(Long id, String sharedURL, String sharedKey) {
            return new SharedDTO.SharedBoardResponse(id, sharedURL, sharedKey);
        }
    }
}
