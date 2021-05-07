package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.domain.model.category.Category;
import com.ddd.moodof.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryDTO.CategoryResponse create(CategoryDTO.CreateCategoryRequest request, Long userId) {
        Optional<Category> categoryExist = categoryRepository.findByNameAndUserId(request.getName(), userId);
        if (categoryExist.isPresent()) throw new IllegalArgumentException("존재하는 카테고리 입니다.");
        Category category = categoryRepository.save(request.toEntity(userId, request.getName()));
        return CategoryDTO.CategoryResponse.from(category);
    }
}
