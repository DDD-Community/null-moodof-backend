package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.BoardDTO;
import org.junit.jupiter.api.Test;

import static com.ddd.moodof.adapter.presentation.BoardController.API_BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardAcceptanceTest extends AcceptanceTest {

    @Test
    void 보드를_생성한다() {
        BoardDTO.CreateBoard request = new BoardDTO.CreateBoard(0L, 1L, "name");
        BoardDTO.BoardResponse response = postWithLogin(request, API_BOARD, BoardDTO.BoardResponse.class, userId);
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getPreviousBoardId()).isEqualTo(request.getPreviousBoardId()),
                () -> assertThat(response.getCategoryId()).isEqualTo(request.getCategoryId()),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getName()).isEqualTo(request.getName()),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getCreatedDate()).isEqualTo(response.getLastModifiedDate())
        );
    }
}
