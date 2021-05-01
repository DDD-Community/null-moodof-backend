package com.ddd.moodof.domain.model.storage.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoragePhotoCreateService {
    private static final String BASE64_PREFIX = "data:";

    private final StoragePhotoRepository storagePhotoRepository;
    private final FileUploader fileUploader;

    public StoragePhoto create(String uri, String representativeColor, Long userId) {
        if (uri.startsWith(BASE64_PREFIX)) {
            String fileUri = fileUploader.upload(uri, userId);
            return storagePhotoRepository.save(new StoragePhoto(null, userId, fileUri, representativeColor, null, null));
        }

        return storagePhotoRepository.save(new StoragePhoto(null, userId, uri, representativeColor, null, null));
    }
}
