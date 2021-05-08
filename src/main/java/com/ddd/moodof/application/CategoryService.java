package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.domain.model.category.Category;
import com.ddd.moodof.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryDTO.CategoryResponse create(CategoryDTO.CreateCategoryRequest request, Long userId) {
        Optional<Category> categoryExist = categoryRepository.findByTitleAndUserId(request.getTitle(), userId);
        if (categoryExist.isPresent()) throw new IllegalArgumentException("존재하는 카테고리 입니다.");
        Category category = categoryRepository.save(request.toEntity(userId, request.getTitle()));
        return CategoryDTO.CategoryResponse.from(category);
    }

    public CategoryDTO.CategoryResponse updateName(CategoryDTO.UpdateNameCategoryRequest request, Long id, Long userId) {
        if(!existsByIdAndUserId(id, userId)) throw new IllegalArgumentException("카테고리 정보가 일치하지 않습니다.");
        Optional<Category> categoryInfo = categoryRepository.findById(id);
        return CategoryDTO.CategoryResponse.from(categoryRepository.save(request.toEntity(categoryInfo.get(), request)));
    }

    private boolean existsByIdAndUserId(Long id, Long userId) {
        return categoryRepository.existsByIdAndUserId(id, userId);
    }

    public void deleteById(Long id, Long userId) {
        if(categoryRepository.existsByIdAndUserId(id, userId)) categoryRepository.deleteById(id);
        else new IllegalArgumentException("존재하지 않는 카테고리입니다. " + id + " / " + userId);
    }

    public List<CategoryDTO.CategoryResponse> findAllByUserId(Long userId){
        List<Category> totalCategories = categoryRepository.findAllByUserId(userId);
        return CategoryDTO.CategoryResponse.listForm(totalCategories);
    }


}
