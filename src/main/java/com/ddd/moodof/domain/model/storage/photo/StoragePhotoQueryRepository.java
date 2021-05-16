package com.ddd.moodof.domain.model.storage.photo;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoragePhotoQueryRepository {
    StoragePhotoDTO.StoragePhotoPageResponse findPageExcludeTrash(Long userId, Pageable pageable, List<Long> tagIds);

    StoragePhotoDTO.StoragePhotoDetailResponse findDetail(Long userId, Long id);
}
