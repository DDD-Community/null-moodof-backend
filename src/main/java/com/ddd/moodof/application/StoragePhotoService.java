package com.ddd.moodof.application;

import com.ddd.moodof.adapter.infrastructure.persistence.PaginationUtils;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoCreator;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoQueryRepository;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoragePhotoService {
    private final StoragePhotoRepository storagePhotoRepository;
    private final StoragePhotoQueryRepository storagePhotoQueryRepository;
    private final StoragePhotoCreator storagePhotoCreator;
    private final PaginationUtils paginationUtils;

    public StoragePhotoDTO.StoragePhotoResponse create(StoragePhotoDTO.CreateStoragePhoto request, Long userId) {
        StoragePhoto saved = storagePhotoCreator.create(request.getUri(), request.getRepresentativeColor(), userId);
        return StoragePhotoDTO.StoragePhotoResponse.from(saved);
    }

    public StoragePhotoDTO.StoragePhotoPageResponse findPage(Long userId, int page, int size, String sortBy, boolean descending, List<Long> tagIds) {
        return storagePhotoQueryRepository.findPageExcludeTrash(userId, PageRequest.of(page, size, paginationUtils.getSort(sortBy, descending)), tagIds);
    }

    public boolean existsByIdAndUserId(Long id, Long userId) {
        return storagePhotoRepository.existsByIdAndUserId(id, userId);
    }

    @Transactional
    public void delete(Long userId, StoragePhotoDTO.DeleteStoragePhotos request) {
        if (!storagePhotoRepository.existsByIdInAndUserId(request.getStoragePhotoIds(), userId)) {
            throw new IllegalArgumentException("요청과 일치하는 보관함 사진이 없습니다. ");
        }
        storagePhotoRepository.deleteAllByIdIn(request.getStoragePhotoIds());
    }

    public StoragePhotoDTO.StoragePhotoDetailResponse findDetail(Long userId, Long id, List<Long> tagIds) {
        return storagePhotoQueryRepository.findDetail(userId, id, tagIds);
    }
}
