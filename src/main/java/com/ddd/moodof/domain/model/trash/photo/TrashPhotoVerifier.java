package com.ddd.moodof.domain.model.trash.photo;

import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TrashPhotoVerifier {
    private final StoragePhotoRepository storagePhotoRepository;

    public TrashPhoto toEntity(Long userId, Long storagePhotoId) {
        if (storagePhotoRepository.existsByIdAndUserId(storagePhotoId, userId)) {
            return new TrashPhoto(null, storagePhotoId, userId, null, null);
        }
        throw new IllegalArgumentException("userId, storagePhotoId와 일치하는 StoragePhoto가 존재하지 않습니다. userId / stroagePhotoId = " + userId + " / " + storagePhotoId);
    }
}
