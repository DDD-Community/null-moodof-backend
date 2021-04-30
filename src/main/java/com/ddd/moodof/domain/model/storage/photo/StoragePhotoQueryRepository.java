package com.ddd.moodof.domain.model.storage.photo;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import org.springframework.data.domain.Pageable;

public interface StoragePhotoQueryRepository {
    StoragePhotoDTO.StoragePhotoPageResponse findPageExcludeTrash(Long userId, Pageable pageable);
}
