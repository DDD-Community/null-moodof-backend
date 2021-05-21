package com.ddd.moodof.domain.model.category;


import com.ddd.moodof.application.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryInitializer {
    public  static final String SUB_TITLE = "님의 카테고리";
    public static final Long INITIAL_PREVIOUS_ID = 0L;
    private final CategoryRepository categoryRepository;

    public Category create(Long userId, String nickName) {
        CategoryDTO.CreateCategoryRequest request = new CategoryDTO.CreateCategoryRequest();
        return categoryRepository.save(request.toEntity(userId, nickName + SUB_TITLE , INITIAL_PREVIOUS_ID));
    }
}
