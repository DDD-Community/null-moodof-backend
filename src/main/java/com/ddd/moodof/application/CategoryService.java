package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.domain.model.category.Category;
import com.ddd.moodof.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDTO.CategoryResponse create(CategoryDTO.CreateCategoryRequest request, Long userId) {
        Category saved = categoryRepository.save(request.toEntity(userId));
        saveCategoryIntermediate(saved, request.getPreviousId());
        return CategoryDTO.CategoryResponse.from(saved);
    }

    public CategoryDTO.CategoryResponse updateTitle(CategoryDTO.UpdateTitleCategoryRequest request, Long id, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 정보가 일치하지 않습니다."));
        category.updateTitle(request.getTitle());
        Category saved = categoryRepository.save(category);
        return CategoryDTO.CategoryResponse.from(saved);
    }

    public void deleteById(Long id, Long userId) {
        categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다. " + id + " / " + userId));
        categoryRepository.deleteById(id);
    }

    public List<CategoryDTO.CategoryResponse> findAllByUserId(Long userId){
        List<Category> totalCategories = categoryRepository.findAllByUserId(userId);
        return CategoryDTO.CategoryResponse.listForm(totalCategories);
    }

    @Transactional
    public CategoryDTO.CategoryResponse updatePreviousId(Long id, CategoryDTO.UpdateOrderCategoryRequest request, Long userId) {
        Category target = findByIdAndUserId(id,userId);
        updatePreviousId(target,request.getPreviousId());
        return CategoryDTO.CategoryResponse.from(categoryRepository.save(target));
    }

    private void saveCategoryIntermediate(Category saved, Long previousId) {
        categoryRepository.findAllByPreviousId(previousId)
                .stream()
                .filter(c -> !c.getId().equals(saved.getId()))
                .findAny()
                .ifPresent(cc -> cc.updatePreviousId(saved.getId()));
    }

    private void updatePreviousId(Category target, Long previousId) {
        Category destination = findByPreviousId(previousId);
        Optional<Category> afterTarget = categoryRepository.findOptionalByPreviousId(target.getId());
        afterTarget.ifPresent(t -> categoryRepository.save(t.updatePreviousId(target.getPreviousId())));
        categoryRepository.save(destination.updatePreviousId(target.getId()));
        target.updatePreviousId(previousId);
    }

    private Category findByPreviousId(Long previousId) {
        return categoryRepository.findOptionalByPreviousId(previousId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 previousId : " + previousId));
    }

    private Category findByIdAndUserId(Long id, Long userId) {
        return categoryRepository.findByIdAndUserId(id,userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID : " + id));
    }
}
