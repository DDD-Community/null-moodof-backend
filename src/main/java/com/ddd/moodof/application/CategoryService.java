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
    private final CategoryRepository categoryRepository;
    private final CategoryQueryRepository categoryQueryRepository;
    private final BoardRepository boardRepository;

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

    @Transactional
    public void deleteById(Long id, Long userId) {
        categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다. " + id + " / " + userId));
        categoryRepository.deleteById(id);
        boardRepository.deleteAllByCategoryId(id);
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

    public List<CategoryDTO.CategoryResponse> findCategoryByUserId(Long userId) {
        List<Category> categoryList = categoryRepository.findCategoryByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("올바른 유저 아이디가 아닙니다. userId " + userId));
        return CategoryDTO.CategoryResponse.listForm(categoryList);
    }

    public List<CategoryDTO.CategoryWithBoardResponse> findCategoryWithBoardByUserId(Long userId) {
        if(!categoryRepository.existsByUserId(userId)){
            throw new IllegalArgumentException("올바른 유저 아이디가 아닙니다. userId " + userId);
        }
        return categoryQueryRepository.findCategoryWithBoardByUserId(userId);
    }


}
