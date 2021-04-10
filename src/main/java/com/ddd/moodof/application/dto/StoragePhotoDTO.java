package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class StoragePhotoDTO {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class Create {
        @NotBlank
        private String uri;
        @NotBlank
        private String representativeColor;

        public StoragePhoto toEntity(Long userId) {
            return new StoragePhoto(null, userId, uri, representativeColor, null, null);
        }
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

        public static Response from(StoragePhoto storagePhoto) {
            return new Response(storagePhoto.getId(), storagePhoto.getUserId(), storagePhoto.getUri(), storagePhoto.getRepresentativeColor(), storagePhoto.getCreatedDate(), storagePhoto.getLastModifiedDate());
        }
    }
}
