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
        CategoryDTO.CategoryResponse response1 = 카테고리_생성(userId, "category-1", 0L);
        CategoryDTO.CategoryResponse response2 = 카테고리_생성(userId, "category-2", response1.getId());
        CategoryDTO.CategoryResponse response = 카테고리_생성(userId, "category-3", response2.getId());

        // then
        assertAll(
                () -> assertThat(response1.getId()).isEqualTo(1L),
                () -> assertThat(response1.getPreviousId()).isEqualTo(0L),
                () -> assertThat(response2.getId()).isEqualTo(2L),
                () -> assertThat(response2.getPreviousId()).isEqualTo(1L),

                () -> assertThat(response.getId()).isEqualTo(3L),
                () -> assertThat(response.getPreviousId()).isEqualTo(2L),
                () -> assertThat(response.getTitle()).isEqualTo("category-3"),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isNotNull()
        );
    }
    @Test
    public void 카테고리_이름_변경() throws Exception {
        // given

        // when
        CategoryDTO.CategoryResponse request = 카테고리_생성(userId, "category-1", 0L);
        CategoryDTO.CategoryResponse response = 카테고리_이름_변경(request.getId(), request.getUserId(), "category-2","title");

        // then
        assertThat(response.getTitle()).isEqualTo("category-2");
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
                () -> assertThat(categoryResponseList.size()).isEqualTo(1),
                () -> assertThat(categoryResponseList.get(0)).usingRecursiveComparison().isEqualTo(two)
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
        CategoryDTO.CategoryResponse unchangedResponse = 카테고리_순서_변경(three.getId(), 1L, userId, "previousId");
        List<CategoryDTO.CategoryResponse> unchangedOrderResponseList = getListWithLogin(API_CATEGORY , CategoryDTO.CategoryResponse.class, userId);
        CategoryDTO.CategoryResponse changedResponse = 카테고리_순서_변경(four.getId(), 3L, userId, "previousId");
        List<CategoryDTO.CategoryResponse> changedOrderResponseList = getListWithLogin(API_CATEGORY , CategoryDTO.CategoryResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(unchangedResponse).isNotNull(),
                () -> assertThat(unchangedResponse.getId()).isEqualTo(three.getId()),
                () -> assertThat(unchangedResponse.getPreviousId()).isEqualTo(1L),

                () -> assertThat(changedResponse).isNotNull(),
                () -> assertThat(changedResponse.getId()).isEqualTo(four.getId()),
                () -> assertThat(changedResponse.getPreviousId()).isEqualTo(3L),

                () -> assertThat(unchangedOrderResponseList.get(0).getId()).isEqualTo(1L),
                () -> assertThat(unchangedOrderResponseList.get(0).getPreviousId()).isEqualTo(0L),
                () -> assertThat(unchangedOrderResponseList.get(1).getId()).isEqualTo(2L),
                () -> assertThat(unchangedOrderResponseList.get(1).getPreviousId()).isEqualTo(3L),
                () -> assertThat(unchangedOrderResponseList.get(2).getId()).isEqualTo(3L),
                () -> assertThat(unchangedOrderResponseList.get(2).getPreviousId()).isEqualTo(1L),
                () -> assertThat(unchangedOrderResponseList.get(3).getId()).isEqualTo(4L),
                () -> assertThat(unchangedOrderResponseList.get(3).getPreviousId()).isEqualTo(2L),

                () -> assertThat(changedOrderResponseList.get(0).getId()).isEqualTo(1L),
                () -> assertThat(changedOrderResponseList.get(0).getPreviousId()).isEqualTo(0L),
                () -> assertThat(changedOrderResponseList.get(1).getId()).isEqualTo(2L),
                () -> assertThat(changedOrderResponseList.get(1).getPreviousId()).isEqualTo(4L),
                () -> assertThat(changedOrderResponseList.get(2).getId()).isEqualTo(3L),
                () -> assertThat(changedOrderResponseList.get(2).getPreviousId()).isEqualTo(1L),
                () -> assertThat(changedOrderResponseList.get(3).getId()).isEqualTo(4L),
                () -> assertThat(changedOrderResponseList.get(3).getPreviousId()).isEqualTo(3L)
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
