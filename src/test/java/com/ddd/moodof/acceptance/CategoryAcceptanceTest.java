package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.CategoryDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ddd.moodof.adapter.presentation.CategoryController.API_CATEGORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CategoryAcceptanceTest extends AcceptanceTest{
    public static final String PREVIOUS_ID = "previousId";
    public static final String TITLE = "title";

    @Test
    public void 카테고리_생성() throws Exception {
        // given

        // when
        CategoryDTO.CategoryResponse one = 카테고리_생성(userId, "category-1", 0L);
        CategoryDTO.CategoryResponse two = 카테고리_생성(userId, "category-2", one.getId());
        CategoryDTO.CategoryResponse response = 카테고리_생성(userId, "category-3", two.getId());

        // then
        assertAll(
                () -> assertThat(one.getId()).isEqualTo(one.getId()),
                () -> assertThat(one.getPreviousId()).isNotNull(),
                () -> assertThat(two.getId()).isEqualTo(two.getId()),
                () -> assertThat(two.getPreviousId()).isEqualTo(one.getId()),

                () -> assertThat(response.getId()).isEqualTo(two.getId()+1),
                () -> assertThat(response.getPreviousId()).isEqualTo(two.getId()),
                () -> assertThat(response.getTitle()).isEqualTo("category-3"),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isNotNull()
        );
    }
    @Test
    public void 카테고리_이름_변경() throws Exception {
        // given
        CategoryDTO.CategoryResponse category = 카테고리_생성(userId, "category-1", 0L);

        // when
        CategoryDTO.UpdateTitleCategoryRequest request = new CategoryDTO.UpdateTitleCategoryRequest("category-2");
        CategoryDTO.CategoryResponse response = putPropertyWithLogin(request, category.getId(), API_CATEGORY, CategoryDTO.CategoryResponse.class, userId, TITLE);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getId()).isEqualTo(category.getId()),
                () -> assertThat(response.getUserId()).isNotNull(),
                () -> assertThat(response.getTitle()).isEqualTo("category-2"),
                () -> assertThat(response.getCreatedDate()).isEqualTo(category.getCreatedDate()),
                () -> assertThat(response.getLastModifiedDate().isAfter(response.getCreatedDate())).isTrue()
        );
    }

    @Test
    public void 카테고리_삭제() throws Exception {
        // given
        CategoryDTO.CategoryResponse one = 카테고리_생성(userId, "category-1",0L);
        CategoryDTO.CategoryResponse two = 카테고리_생성(userId, "category-2", 1L);

        // when
        deleteWithLogin(API_CATEGORY, one.getId(), one.getUserId());
        List<CategoryDTO.CategoryResponse> categoryResponseList = getListWithLogin(API_CATEGORY, CategoryDTO.CategoryResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(categoryResponseList.get(0).getUserId()).isNotNull(),
                () -> assertThat(categoryResponseList.get(0).getId()).isEqualTo(two.getId()),
                () -> assertThat(categoryResponseList.size()).isEqualTo(1),
                () -> assertThat(categoryResponseList.get(0)).usingRecursiveComparison().isEqualTo(two),
                () -> assertThat(categoryResponseList.get(0).getCreatedDate()).isNotNull(),
                () -> assertThat(categoryResponseList.get(0).getLastModifiedDate()).isNotNull()
        );

    }

    @Test
    public void 카테고리_순서_변경() throws Exception {

        // given
        CategoryDTO.CategoryResponse one = 카테고리_생성(userId, "category-1", 0L);
        CategoryDTO.CategoryResponse two = 카테고리_생성(userId, "category-2", one.getId());
        CategoryDTO.CategoryResponse three = 카테고리_생성(userId, "category-3", two.getId());
        CategoryDTO.CategoryResponse four = 카테고리_생성(userId, "category-4", three.getId());

        // when
        CategoryDTO.CategoryResponse firstChangedResponse = 카테고리_순서_변경(three.getId(), one.getId(), four.getUserId(), PREVIOUS_ID);
        List<CategoryDTO.CategoryResponse> firstChangedOrderResponseList = getListWithLogin(API_CATEGORY , CategoryDTO.CategoryResponse.class, firstChangedResponse.getUserId());
        CategoryDTO.CategoryResponse secondChangedResponse = 카테고리_순서_변경(four.getId(), three.getId(), firstChangedResponse.getUserId(), PREVIOUS_ID);
        List<CategoryDTO.CategoryResponse> secondChangedOrderResponseList = getListWithLogin(API_CATEGORY , CategoryDTO.CategoryResponse.class, secondChangedResponse.getUserId());

        // then
        assertAll(
                () -> assertThat(firstChangedResponse).isNotNull(),
                () -> assertThat(firstChangedResponse.getId()).isEqualTo(three.getId()),
                () -> assertThat(firstChangedResponse.getPreviousId()).isEqualTo(two.getPreviousId()),

                () -> assertThat(secondChangedResponse).isNotNull(),
                () -> assertThat(secondChangedResponse.getId()).isEqualTo(four.getId()),
                () -> assertThat(secondChangedResponse.getPreviousId()).isEqualTo(four.getPreviousId()),

                () -> assertThat(firstChangedOrderResponseList.get(0).getId()).isEqualTo(one.getId()),
                () -> assertThat(firstChangedOrderResponseList.get(0).getPreviousId()).isEqualTo(0L),
                () -> assertThat(firstChangedOrderResponseList.get(1).getId()).isEqualTo(two.getId()),
                () -> assertThat(firstChangedOrderResponseList.get(1).getPreviousId()).isEqualTo(three.getId()),
                () -> assertThat(firstChangedOrderResponseList.get(2).getId()).isEqualTo(three.getId()),
                () -> assertThat(firstChangedOrderResponseList.get(2).getPreviousId()).isEqualTo(one.getId()),
                () -> assertThat(firstChangedOrderResponseList.get(3).getId()).isEqualTo(four.getId()),
                () -> assertThat(firstChangedOrderResponseList.get(3).getPreviousId()).isEqualTo(two.getId()),

                () -> assertThat(secondChangedOrderResponseList.get(0).getId()).isEqualTo(one.getId()),
                () -> assertThat(secondChangedOrderResponseList.get(0).getPreviousId()).isEqualTo(0L),
                () -> assertThat(secondChangedOrderResponseList.get(1).getId()).isEqualTo(two.getId()),
                () -> assertThat(secondChangedOrderResponseList.get(1).getPreviousId()).isEqualTo(four.getId()),
                () -> assertThat(secondChangedOrderResponseList.get(2).getId()).isEqualTo(three.getId()),
                () -> assertThat(secondChangedOrderResponseList.get(2).getPreviousId()).isEqualTo(one.getId()),
                () -> assertThat(secondChangedOrderResponseList.get(3).getId()).isEqualTo(four.getId()),
                () -> assertThat(secondChangedOrderResponseList.get(3).getPreviousId()).isEqualTo(three.getId())
        );
    }

    @Test
    public void 카테고리_조회() throws Exception {
        // given
        CategoryDTO.CategoryResponse one = 카테고리_생성(userId, "category-1",0L);
        CategoryDTO.CategoryResponse two = 카테고리_생성(userId, "category-2", 1L);

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
