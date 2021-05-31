package com.ddd.moodof.domain.model.storage.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoragePhotoCreator {
    private static final String BASE64_PREFIX = "data:";
    private static final int MAX_STORAGE_PHOTO_COUNT = 600;

    private final StoragePhotoRepository storagePhotoRepository;
    private final FileUploader fileUploader;

    public StoragePhoto create(String uri, String representativeColor, Long userId) {
        if (storagePhotoRepository.countByUserId(userId) >= MAX_STORAGE_PHOTO_COUNT) {
            throw new IllegalStateException("이미지 보관함의 이미지 최대 개수는 600개 입니다.");
        }

        if (uri.startsWith(BASE64_PREFIX)) {
            String fileUri = fileUploader.upload(uri, userId);
            return storagePhotoRepository.save(new StoragePhoto(null, userId, fileUri, representativeColor, null, null));
        }

        return storagePhotoRepository.save(new StoragePhoto(null, userId, uri, representativeColor, null, null));
    }
}
