package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoragePhotoService {
    private final StoragePhotoRepository storagePhotoRepository;

    public StoragePhotoDTO.StoragePhotoResponse create(StoragePhotoDTO.CreateStoragePhoto request, Long userId) {
        StoragePhoto storagePhoto = request.toEntity(userId);
        StoragePhoto saved = storagePhotoRepository.save(storagePhoto);
        return StoragePhotoDTO.StoragePhotoResponse.from(saved);
    }

    public List<StoragePhotoDTO.StoragePhotoResponse> findPage(Long userId, Pageable pageable) {
        List<StoragePhoto> storagePhotos = storagePhotoRepository.findPageByUserId(userId, pageable);
        return StoragePhotoDTO.StoragePhotoResponse.listFrom(storagePhotos);
    }
}
