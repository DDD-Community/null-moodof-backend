package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.trash.photo.TrashPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TrashPhotoDTO {
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class CreateTrashPhoto {
        private Long storagePhotoId;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class TrashPhotoResponse {
        private Long id;
        private Long storagePhotoId;
        private Long userId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static TrashPhotoResponse from(TrashPhoto trashPhoto) {
            return new TrashPhotoResponse(trashPhoto.getId(), trashPhoto.getStoragePhotoId(), trashPhoto.getUserId(), trashPhoto.getCreatedDate(), trashPhoto.getLastModifiedDate());
        }
    }
}
