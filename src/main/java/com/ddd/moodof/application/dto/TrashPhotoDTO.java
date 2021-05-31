package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.trash.photo.TrashPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TrashPhotoDTO {
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class CreateTrashPhotos {
        private List<Long> storagePhotoIds;

        public List<TrashPhoto> toEntities(Long userId) {
            return storagePhotoIds.stream()
                    .map(storagePhotoId -> new TrashPhoto(null, storagePhotoId, userId, null, null))
                    .collect(Collectors.toList());
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
    public static class TrashPhotoCreatedResponse {
        private Long id;
        private Long storagePhotoId;
        private Long userId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static List<TrashPhotoCreatedResponse> listOf(List<TrashPhoto> trashPhotos) {
            return trashPhotos.stream()
                    .map(TrashPhotoCreatedResponse::of)
                    .collect(Collectors.toList());
        }

        public static TrashPhotoCreatedResponse of(TrashPhoto trashPhoto) {
            return new TrashPhotoCreatedResponse(trashPhoto.getId(), trashPhoto.getStoragePhotoId(), trashPhoto.getUserId(), trashPhoto.getCreatedDate(), trashPhoto.getLastModifiedDate());
        }
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class TrashPhotoPageResponse {
        private long totalPageCount;
        private List<TrashPhotoResponse> responses;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class CancelTrashPhotos {
        private List<Long> trashPhotoIds;
    }
}
