package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
                () -> assertThat(response.getName()).isEqualTo("category-2"),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isNotNull()
        );

    }
    @Test
    public void 카테고리_삭제() throws Exception {
        // given

        // when

        // then
    }

    @Test
    public void 카테고리_순서_변경() throws Exception {
        // given

        // when

        // then
    }

    @Test
    public void 카테고리_이름_변경() throws Exception {
        // given

        // when

        // then
    }
    @Test
    public void 카테고리_조회() throws Exception {
        // given

        // when

        // then
    }


}
