package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.trash.photo.TrashPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class TrashPhotoDTO {
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class CreateTrashPhoto {
        private Long storagePhotoId;

        public TrashPhoto toEntity(Long userId) {
            return new TrashPhoto(null, storagePhotoId, userId, null, null);
        }
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class TrashPhotoResponse {
        private Long id;
        private StoragePhotoDTO.StoragePhotoResponse storagePhoto;
        private Long userId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static TrashPhotoResponse of(TrashPhoto trashPhoto, StoragePhoto storagePhoto) {
            return new TrashPhotoResponse(trashPhoto.getId(), StoragePhotoDTO.StoragePhotoResponse.from(storagePhoto), trashPhoto.getUserId(), trashPhoto.getCreatedDate(), trashPhoto.getLastModifiedDate());
        }
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class TrashPhotoPageResponse {
        private long totalPageCount;
        private List<TrashPhotoResponse> responses;
    }
}
