package com.ddd.moodof.domain.model.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.ddd.moodof.application.BoardService.MAX_BOARD_IN_CATEGORY_COUNT;

@RequiredArgsConstructor
@Service
public class BoardSequenceUpdater {
    private final BoardRepository boardRepository;

    @Transactional
    public Board update(Board board, Long previousBoardId, Long categoryId, Long userId) {
        if (board.isCategoryNotEqual(categoryId) && boardRepository.countByCategoryId(categoryId) >= MAX_BOARD_IN_CATEGORY_COUNT) {
            throw new IllegalStateException("하나의 카테고리에 생성할 수 있는 보드 최대 개수는 10개 입니다.");
        }

        boardRepository.findByPreviousBoardId(board.getId())
                .ifPresent(it -> it.changePreviousBoardId(board.getPreviousBoardId(), userId));

        boardRepository.findByPreviousBoardIdAndCategoryId(previousBoardId, categoryId)
                .ifPresent(it -> it.changePreviousBoardId(board.getId(), userId));

        board.updateSequence(previousBoardId, categoryId, userId);

        return boardRepository.save(board);
    }
}
