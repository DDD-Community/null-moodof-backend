package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class UserDTO {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String email;
        private String nickname;
        private String profileUrl;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static UserResponse from(User user) {
            return new UserResponse(user.getId(), user.getEmail(), user.getNickname(), user.getProfileUrl(), user.getCreatedDate(), user.getLastModifiedDate());
        }
    }
}
