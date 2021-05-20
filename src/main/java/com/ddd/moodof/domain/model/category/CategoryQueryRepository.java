package com.ddd.moodof.domain.model.category;

import com.ddd.moodof.application.dto.CategoryDTO;

import java.util.List;

public interface CategoryQueryRepository {
    List<CategoryDTO.CategoryWithBoardResponse> findCategoryWithBoardByUserId(Long userId);
}
