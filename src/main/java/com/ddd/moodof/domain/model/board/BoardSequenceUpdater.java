package com.ddd.moodof.domain.model.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BoardSequenceUpdater {
    private final BoardRepository boardRepository;

    @Transactional
    public Board update(Board board, Long previousBoardId, Long categoryId, Long userId) {
        boardRepository.findByPreviousBoardId(board.getId())
                .ifPresent(it -> it.changePreviousBoardId(board.getPreviousBoardId(), userId));

        boardRepository.findByPreviousBoardIdAndCategoryId(previousBoardId, categoryId)
                .ifPresent(it -> it.changePreviousBoardId(board.getId(), userId));

        board.updateSequence(previousBoardId, categoryId, userId);

        return boardRepository.save(board);
    }
}
