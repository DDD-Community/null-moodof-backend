package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.domain.model.board.Board;
import com.ddd.moodof.domain.model.board.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ddd.moodof.adapter.presentation.BoardController.API_BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardAcceptanceTest extends AcceptanceTest {
    private CategoryDTO.CategoryResponse category;
    private CategoryDTO.CategoryResponse category2;

    @Autowired
    private BoardRepository boardRepository;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        category = 카테고리_생성(userId, "title", 0L);
        category2 = 카테고리_생성(userId, "title", category.getId());
    }

    @Test
    void 보드를_생성한다() {
        // when
        long previousBoardId = 0L;
        String name = "name";
        BoardDTO.BoardResponse response = 보드_생성(userId, previousBoardId, category.getId(), name);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getPreviousBoardId()).isEqualTo(previousBoardId),
                () -> assertThat(response.getCategoryId()).isEqualTo(category.getId()),
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
        String name = "name";
        BoardDTO.BoardResponse board = 보드_생성(userId, previousBoardId, category.getId(), name);

        // when
        String changed = "changed";
        BoardDTO.ChangeBoardName request = new BoardDTO.ChangeBoardName(changed);
        BoardDTO.BoardResponse response = putPropertyWithLogin(request, board.getId(), API_BOARD, BoardDTO.BoardResponse.class, userId, "name");

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

    @Test
    void 보드_순서를_변경한다() {
        // given
        BoardDTO.BoardResponse first = 보드_생성(userId, 0L, category.getId(), "name");
        BoardDTO.BoardResponse second = 보드_생성(userId, first.getId(), category.getId(), "name");
        BoardDTO.BoardResponse third = 보드_생성(userId, second.getId(), category.getId(), "name");

        // when
        long previousBoardId = 0L;
        BoardDTO.ChangeBoardSequence request = new BoardDTO.ChangeBoardSequence(category.getId(), previousBoardId);
        BoardDTO.BoardResponse response = putPropertyWithLogin(request, second.getId(), API_BOARD, BoardDTO.BoardResponse.class, userId, "previousBoardId");

        Board thirdBoard = boardRepository.findById(third.getId()).get();
        Board firstBoard = boardRepository.findById(first.getId()).get();

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(second.getId()),
                () -> assertThat(response.getPreviousBoardId()).isEqualTo(previousBoardId),
                () -> assertThat(response.getLastModifiedDate().isAfter(response.getCreatedDate())).isTrue(),

                () -> assertThat(thirdBoard.getPreviousBoardId()).isEqualTo(first.getId()),
                () -> assertThat(thirdBoard.getLastModifiedDate().isAfter(third.getLastModifiedDate())).isTrue(),

                () -> assertThat(firstBoard.getPreviousBoardId()).isEqualTo(second.getId()),
                () -> assertThat(firstBoard.getLastModifiedDate().isAfter(first.getLastModifiedDate())).isTrue()
        );
    }

    @Test
    void 보드_순서를_변경한다_first_to_second() {
        // given
        BoardDTO.BoardResponse first = 보드_생성(userId, 0L, category.getId(), "name");
        BoardDTO.BoardResponse second = 보드_생성(userId, first.getId(), category.getId(), "name");
        BoardDTO.BoardResponse third = 보드_생성(userId, second.getId(), category.getId(), "name");

        // when
        long previousBoardId = 2L;
        BoardDTO.ChangeBoardSequence request = new BoardDTO.ChangeBoardSequence(category.getId(), previousBoardId);
        BoardDTO.BoardResponse response = putPropertyWithLogin(request, first.getId(), API_BOARD, BoardDTO.BoardResponse.class, userId, "previousBoardId");

        Board secondBoard = boardRepository.findById(second.getId()).get();
        Board thirdBoard = boardRepository.findById(third.getId()).get();

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(first.getId()),
                () -> assertThat(response.getPreviousBoardId()).isEqualTo(previousBoardId),
                () -> assertThat(response.getLastModifiedDate().isAfter(response.getCreatedDate())).isTrue(),

                () -> assertThat(secondBoard.getPreviousBoardId()).isEqualTo(0L),
                () -> assertThat(secondBoard.getLastModifiedDate().isAfter(second.getLastModifiedDate())).isTrue(),

                () -> assertThat(thirdBoard.getPreviousBoardId()).isEqualTo(first.getId()),
                () -> assertThat(thirdBoard.getLastModifiedDate().isAfter(third.getLastModifiedDate())).isTrue()
        );
    }

    @Test
    void 보드_순서를_변경한다_cross_category() {
        // given
        BoardDTO.BoardResponse first = 보드_생성(userId, 0L, category.getId(), "name");
        BoardDTO.BoardResponse second = 보드_생성(userId, first.getId(), category.getId(), "name");
        BoardDTO.BoardResponse third = 보드_생성(userId, second.getId(), category.getId(), "name");
        BoardDTO.BoardResponse differentCategoryFirst = 보드_생성(userId, 0L, category2.getId(), "name");
        BoardDTO.BoardResponse differentCategorySecond = 보드_생성(userId, differentCategoryFirst.getId(), category2.getId(), "name");
        BoardDTO.BoardResponse differentCategoryThird = 보드_생성(userId, differentCategorySecond.getId(), category2.getId(), "name");

        // when
        long previousBoardId = second.getId();
        BoardDTO.ChangeBoardSequence request = new BoardDTO.ChangeBoardSequence(category.getId(), previousBoardId);
        BoardDTO.BoardResponse response = putPropertyWithLogin(request, differentCategorySecond.getId(), API_BOARD, BoardDTO.BoardResponse.class, userId, "previousBoardId");

        Board thirdBoard = boardRepository.findById(third.getId()).get();
        Board differentCategoryThirdBoard = boardRepository.findById(differentCategoryThird.getId()).get();

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(differentCategorySecond.getId()),
                () -> assertThat(response.getPreviousBoardId()).isEqualTo(previousBoardId),
                () -> assertThat(response.getLastModifiedDate().isAfter(response.getCreatedDate())).isTrue(),

                () -> assertThat(differentCategoryThirdBoard.getPreviousBoardId()).isEqualTo(differentCategoryFirst.getId()),
                () -> assertThat(differentCategoryThirdBoard.getLastModifiedDate().isAfter(second.getLastModifiedDate())).isTrue(),

                () -> assertThat(thirdBoard.getPreviousBoardId()).isEqualTo(differentCategorySecond.getId()),
                () -> assertThat(thirdBoard.getLastModifiedDate().isAfter(third.getLastModifiedDate())).isTrue()
        );
    }
}
