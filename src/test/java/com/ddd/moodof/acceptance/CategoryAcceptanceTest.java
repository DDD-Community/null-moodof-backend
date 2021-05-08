package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ddd.moodof.adapter.presentation.CategoryController.API_CATEGORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CategoryAcceptanceTest extends AcceptanceTest{
    private Long userId;
    @BeforeEach
    void setUp(){
        super.setUp();
        User user = signUp();
        userId = user.getId();
    }

    @Test
    public void 카테고리_생성() throws Exception {
        // given
        // when
        카테고리_생성(userId, "category-1");
        CategoryDTO.CategoryResponse response = 카테고리_생성(userId, "category-2");

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getUserId()).isEqualTo(1L),
                () -> assertThat(response.getTitle()).isEqualTo("category-2"),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isNotNull()
        );
    }
    @Test
    public void 카테고리_이름_변경() throws Exception {
        // given

        // when
        CategoryDTO.CategoryResponse request = 카테고리_생성(userId, "category-1");
        CategoryDTO.CategoryResponse response = 카테고리_이름_변경(request.getId(), request.getUserId(), "category-2");

        // then
        assertThat(response.getTitle()).isEqualTo("category-2");
    }

    @Test
    public void 카테고리_삭제() throws Exception {
        // given
        CategoryDTO.CategoryResponse one = 카테고리_생성(userId, "category-1");
        CategoryDTO.CategoryResponse two = 카테고리_생성(userId, "category-2");

        // when

        deleteWithLogin(API_CATEGORY, one.getId(), one.getUserId());
        List<CategoryDTO.CategoryResponse> categoryResponseList = getListWithLogin(API_CATEGORY, CategoryDTO.CategoryResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(categoryResponseList.size()).isEqualTo(1),
                () -> assertThat(categoryResponseList.get(0)).usingRecursiveComparison().isEqualTo(two)
        );


    }

    @Test
    public void 카테고리_순서_변경() throws Exception {
        // given

        // when

        // then
    }


    @Test
    public void 카테고리_조회() throws Exception {
        // given
        CategoryDTO.CategoryResponse one = 카테고리_생성(userId, "category-1");
        CategoryDTO.CategoryResponse two = 카테고리_생성(userId, "category-2");
        // when

        List<CategoryDTO.CategoryResponse> categoryResponseList = getListWithLogin(API_CATEGORY, CategoryDTO.CategoryResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(categoryResponseList.size()).isEqualTo(2),
                () -> assertThat(categoryResponseList.get(0)).usingRecursiveComparison().isEqualTo(one),
                () -> assertThat(categoryResponseList.get(1)).usingRecursiveComparison().isEqualTo(two)
        );
    }
}
