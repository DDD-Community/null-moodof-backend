package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoragePhotoService {
    private final StoragePhotoRepository storagePhotoRepository;

    public StoragePhotoDTO.Response create(StoragePhotoDTO.Create request, Long userId) {
        StoragePhoto storagePhoto = request.toEntity(userId);
        StoragePhoto saved = storagePhotoRepository.save(storagePhoto);
        return StoragePhotoDTO.Response.from(saved);
    }
}
