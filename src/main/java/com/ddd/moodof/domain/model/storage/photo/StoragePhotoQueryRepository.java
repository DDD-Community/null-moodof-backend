package com.ddd.moodof.domain.model.storage.photo;

import com.ddd.moodof.application.dto.StoragePhotoDTO;

public interface StoragePhotoQueryRepository {
    StoragePhotoDTO.StoragePhotoPageResponse findPageExcludeTrash(Long userId, int page, int size, String sortBy, boolean descending);
}
