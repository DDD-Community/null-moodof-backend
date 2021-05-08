package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.BoardDTO;
import org.junit.jupiter.api.Test;

import static com.ddd.moodof.adapter.presentation.BoardController.API_BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardAcceptanceTest extends AcceptanceTest {

    @Test
    void 보드를_생성한다() {
        // given
        // TODO: 2021/05/05 카테고리 생성 후 변경
        long categoryId = 1L;

        // when
        long previousBoardId = 0L;
        String name = "name";
        BoardDTO.BoardResponse response = 보드_생성(userId, previousBoardId, categoryId, name);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getPreviousBoardId()).isEqualTo(previousBoardId),
                () -> assertThat(response.getCategoryId()).isEqualTo(categoryId),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getName()).isEqualTo(name),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getCreatedDate()).isEqualTo(response.getLastModifiedDate())
        );
    }

    @Test
    void 보드_이름을_수정한다() {
        // given
        long previousBoardId = 0L;
        // TODO: 2021/05/05 카테고리 생성 후 변경
        long categoryId = 1L;
        String name = "name";
        BoardDTO.BoardResponse board = 보드_생성(userId, previousBoardId, categoryId, name);

        // when
        String changed = "changed";
        BoardDTO.ChangeBoardName request = new BoardDTO.ChangeBoardName(changed);
        BoardDTO.BoardResponse response = putWithLogin(request, board.getId(), API_BOARD, BoardDTO.BoardResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(board.getId()),
                () -> assertThat(response.getPreviousBoardId()).isEqualTo(board.getPreviousBoardId()),
                () -> assertThat(response.getCategoryId()).isEqualTo(board.getCategoryId()),
                () -> assertThat(response.getUserId()).isEqualTo(board.getUserId()),
                () -> assertThat(response.getName()).isEqualTo(changed),
                () -> assertThat(response.getCreatedDate()).isEqualTo(board.getCreatedDate()),
                () -> assertThat(response.getLastModifiedDate().isAfter(response.getCreatedDate())).isTrue()
        );
    }
}
