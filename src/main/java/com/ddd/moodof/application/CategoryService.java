package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.domain.model.board.BoardRepository;
import com.ddd.moodof.domain.model.category.Category;
import com.ddd.moodof.domain.model.category.CategoryQueryRepository;
import com.ddd.moodof.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private static final int MAX_CATEGORY_COUNT = 10;

    private final CategoryRepository categoryRepository;
    private final CategoryQueryRepository categoryQueryRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CategoryDTO.CategoryResponse create(CategoryDTO.CreateCategoryRequest request, Long userId) {
        if (categoryRepository.countByUserId(userId) >= MAX_CATEGORY_COUNT) {
            throw new IllegalStateException("생성할 수 있는 카테고리의 최대 개수는 10개 입니다.");
        }

        Category saved = categoryRepository.save(request.toEntity(userId));
        saveCategoryIntermediate(userId, saved, request.getPreviousId());
        return CategoryDTO.CategoryResponse.from(saved);
    }

    public CategoryDTO.CategoryResponse updateTitle(CategoryDTO.UpdateTitleCategoryRequest request, Long id, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 정보가 일치하지 않습니다."));
        category.updateTitle(request.getTitle());
        Category saved = categoryRepository.save(category);
        return CategoryDTO.CategoryResponse.from(saved);
    }

    @Transactional
    public void deleteById(Long id, Long userId) {
        categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다. " + id + " / " + userId));
        categoryRepository.deleteById(id);
        boardRepository.deleteAllByCategoryId(id);
    }

    @Transactional
    public CategoryDTO.CategoryResponse updatePreviousId(Long id, CategoryDTO.UpdateOrderCategoryRequest request, Long userId) {
        Category target = findByIdAndUserId(id, userId);
        updatePreviousId(userId, target, request.getPreviousId());
        return CategoryDTO.CategoryResponse.from(categoryRepository.save(target));
    }

    private void saveCategoryIntermediate(Long userId, Category saved, Long previousId) {
        categoryRepository.findByUserIdAndPreviousIdAndIdNot(userId, previousId, saved.getId())
                .ifPresent(cc -> cc.updatePreviousId(saved.getId()));
    }

    private void updatePreviousId(Long userId, Category target, Long previousId) {
        Category destination = findByUserIdAndPreviousId(userId, previousId);
        Optional<Category> afterTarget = categoryRepository.findByUserIdAndPreviousId(userId, target.getId());
        afterTarget.ifPresent(t -> categoryRepository.save(t.updatePreviousId(target.getPreviousId())));
        categoryRepository.save(destination.updatePreviousId(target.getId()));
        target.updatePreviousId(previousId);
    }

    private Category findByUserIdAndPreviousId(Long userId, Long previousId) {
        return categoryRepository.findByUserIdAndPreviousId(userId, previousId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 previousId : " + previousId));
    }

    private Category findByIdAndUserId(Long id, Long userId) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID : " + id));
    }

    public List<CategoryDTO.CategoryWithBoardResponse> findAllByUserId(Long userId) {
        return categoryQueryRepository.findAllByUserId(userId);
    }
}
