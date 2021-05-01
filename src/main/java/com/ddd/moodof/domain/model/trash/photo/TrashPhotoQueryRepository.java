package com.ddd.moodof.domain.model.trash.photo;

import com.ddd.moodof.application.dto.TrashPhotoDTO;
import org.springframework.data.domain.Pageable;

public interface TrashPhotoQueryRepository {
    TrashPhotoDTO.TrashPhotoPageResponse findPage(Long userId, Pageable pageable);
}
