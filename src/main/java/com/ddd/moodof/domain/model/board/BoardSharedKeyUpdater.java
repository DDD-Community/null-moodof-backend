package com.ddd.moodof.domain.model.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardSharedKeyUpdater {
    private final BoardRepository boardRepository;
    public Board update(Board board, String sharedKey, Long userId){
        board.updateSharedkey(sharedKey, userId);
        return boardRepository.save(board);
    }
}
