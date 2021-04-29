package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StoragePhotoDTO {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class CreateStoragePhoto {
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
    public static class StoragePhotoResponse {
        private Long id;
        private Long userId;
        private String uri;
        private String representativeColor;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static StoragePhotoResponse from(StoragePhoto storagePhoto) {
            return new StoragePhotoResponse(storagePhoto.getId(), storagePhoto.getUserId(), storagePhoto.getUri(), storagePhoto.getRepresentativeColor(), storagePhoto.getCreatedDate(), storagePhoto.getLastModifiedDate());
        }

        public static List<StoragePhotoResponse> listFrom(List<StoragePhoto> storagePhotos) {
            return storagePhotos.stream()
                    .map(StoragePhotoResponse::from)
                    .collect(Collectors.toList());
        }

    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class StoragePhotoPageResponse {
        private long totalPageCount;
        private List<StoragePhotoResponse> storagePhotos;

    }
}
