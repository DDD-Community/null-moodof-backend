package com.ddd.moodof.domain.model.trash.photo;

import com.ddd.moodof.application.dto.TrashPhotoDTO;

public interface TrashPhotoQueryRepository {
    TrashPhotoDTO.TrashPhotoPageResponse findPage(Long userId, int page, int size, String sortBy, boolean descending);
}
