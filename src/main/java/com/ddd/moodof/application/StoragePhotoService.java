package com.ddd.moodof.application;

import com.ddd.moodof.adapter.infrastructure.repository.PaginationUtils;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoQueryRepository;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoragePhotoService {
    private final StoragePhotoRepository storagePhotoRepository;
    private final StoragePhotoQueryRepository storagePhotoQueryRepository;
    private final PaginationUtils paginationUtils;

    public StoragePhotoDTO.StoragePhotoResponse create(StoragePhotoDTO.CreateStoragePhoto request, Long userId) {
        StoragePhoto storagePhoto = request.toEntity(userId);
        StoragePhoto saved = storagePhotoRepository.save(storagePhoto);
        return StoragePhotoDTO.StoragePhotoResponse.from(saved);
    }

    public StoragePhotoDTO.StoragePhotoPageResponse findPage(Long userId, int page, int size, String sortBy, boolean descending) {
        return storagePhotoQueryRepository.findPageExcludeTrash(userId, PageRequest.of(page, size, paginationUtils.getSort(sortBy, descending)));
    }

    public boolean existsByIdAndUserId(Long id, Long userId) {
        return storagePhotoRepository.existsByIdAndUserId(id, userId);
    }

    public void deleteById(Long userId, Long id) {
        if (!storagePhotoRepository.existsByIdAndUserId(id, userId)) {
            throw new IllegalArgumentException("요청과 일치하는 보관함 사진이 없습니다. id / userId: " + id + " / " + userId);
        }
        storagePhotoRepository.deleteById(id);
    }
}
