package com.ddd.moodof.domain.model.category;


import com.ddd.moodof.application.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
@RequiredArgsConstructor
@Repository
public class CategoryInitializer {
    public  static final String SUBTITLE = "님의 카테고리";
    public static final Long INITIALIZE_PREVIOUS_ID = 0L;
    private final CategoryRepository categoryRepository;

    @Tra
    public Category create(Long userId, String nickName) {
        CategoryDTO.CreateCategoryRequest request = new CategoryDTO.CreateCategoryRequest();
        return categoryRepository.save(request.toEntity(userId, nickName + SUBTITLE , INITIALIZE_PREVIOUS_ID));
    }
}
