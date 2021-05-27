package com.ddd.moodof.acceptance;

import com.ddd.moodof.adapter.presentation.PublicController;
import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.application.dto.SharedDTO;
import org.junit.jupiter.api.Test;

import java.util.List;


public class SharedAcceptanceTest extends AcceptanceTest{
    @Test
    public void 공유_URL_생성() throws Exception {
        // given
        long previousBoardId = 0L;
        String name = "name";
        CategoryDTO.CategoryResponse category = 카테고리_생성(userId, "title", 0L);
        BoardDTO.BoardResponse response = 보드_생성(userId, previousBoardId, category.getId(), name);

        // when
        SharedDTO.SharedBoardResponse response1 = 보드_공유하기(response.getId(), userId);

        // then

    }
    @Test
    public void 권한없이_보드_조회() throws Exception {
        // given
        long previousBoardId = 0L;
        String name = "name";
        CategoryDTO.CategoryResponse category = 카테고리_생성(userId, "title", 0L);
        BoardDTO.BoardResponse board = 보드_생성(userId, previousBoardId, category.getId(), name);
        SharedDTO.SharedBoardResponse response = 보드_공유하기(board.getId(), userId);

        List<BoardDTO.BoardResponse> publicListWithLogin = getPublicListWithLogin(PublicController.API_PUBLIC + PublicController.BOARDS, BoardDTO.BoardResponse.class, response.getSharedKey());

        // when

        // then
    }
}
