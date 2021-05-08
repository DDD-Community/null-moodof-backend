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
        findOptionalById(request, userId);
        Category saved = categoryRepository.save(request.toEntity(userId, request.getTitle(), request.getTargetId(), request.getPreviousId()));
        insertOrder(saved, request.getPreviousId(), request.getTargetId());
        return CategoryDTO.CategoryResponse.from(saved);
    }

    private void insertOrder(Category saved, Long previousId, Long targetId) {
        categoryRepository.findAllByPreviousId(previousId)
                .stream()
                .filter(c -> !c.getId().equals(saved.getId()))
                .findAny()
                .ifPresent(cc -> cc.updatePreviousId(saved.getId(), targetId));
    }

    private void findOptionalById(CategoryDTO.CreateCategoryRequest request, Long userId) {
        Optional<Category> category = categoryRepository.findByTitleAndUserId(request.getTitle(), userId);
        category.ifPresent(s -> { throw new IllegalArgumentException("존재하는 카테고리 입니다."); });
    }

    @Transactional
    public CategoryDTO.CategoryResponse updateTitle(CategoryDTO.UpdateTitleCategoryRequest request, Long id, Long userId) {
        if(!existsByIdAndUserId(id, userId)) throw new IllegalArgumentException("카테고리 정보가 일치하지 않습니다.");
        Optional<Category> categoryInfo = categoryRepository.findById(id);
        return CategoryDTO.CategoryResponse.from(categoryRepository.save(request.toEntity(categoryInfo.get(), request)));
    }

    @Transactional
    public void deleteById(Long id, Long userId) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다. " + id + " / " + userId));
        categoryRepository.deleteById(id);
    }

    public List<CategoryDTO.CategoryResponse> findAllByUserId(Long userId){
        List<Category> totalCategories = categoryRepository.findAllByUserId(userId);
        return CategoryDTO.CategoryResponse.listForm(totalCategories);
    }

    @Transactional
    public CategoryDTO.CategoryResponse updateOrder(Long id, CategoryDTO.UpdateOrderCategoryRequest request) {
        Category category = findById(id);
        updateOrder(category,request.getPreviousId(), request.getTargetId());
        return CategoryDTO.CategoryResponse.from(categoryRepository.save(category));
    }

    private boolean existsByIdAndUserId(Long id, Long userId) {
        return categoryRepository.existsByIdAndUserId(id, userId);
    }

    private void updateOrder(Category target, Long previousId, Long targetId) {
        Category destination = findByPreviousId(previousId);
        Optional<Category> afterTarget = categoryRepository.findOptionalByPreviousId(target.getId());
        afterTarget.ifPresent(t ->
                categoryRepository.save(t.updatePreviousId(target.getPreviousId(),targetId)));
        categoryRepository.save(destination.updatePreviousId(target.getId(), targetId));
        target.updatePreviousId(previousId, targetId);
    }

    private Category findByPreviousId(Long previousId) {
        return categoryRepository.findOptionalByPreviousId(previousId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 previousId : " + previousId));
    }

    private Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID : " + id));
    }
}
