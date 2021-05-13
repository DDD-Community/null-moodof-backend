package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.domain.model.board.Board;
import com.ddd.moodof.domain.model.board.BoardRepository;
import com.ddd.moodof.domain.model.board.BoardVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardVerifier boardVerifier;

    @Transactional
    public BoardDTO.BoardResponse create(Long userId, BoardDTO.CreateBoard request) {
        Board board = boardVerifier.toEntity(request.getPreviousBoardId(), request.getCategoryId(), request.getName(), userId);
        Board saved = boardRepository.save(board);

        boardRepository.findByPreviousBoardIdAndIdNot(request.getPreviousBoardId(), saved.getId())
                .ifPresent(it -> it.updatePreviousBoardId(userId, saved.getId()));

        return BoardDTO.BoardResponse.from(saved);
    }

    public BoardDTO.BoardResponse changeName(Long userId, Long id, BoardDTO.ChangeBoardName request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보드입니다. id = " + id));

        board.changeName(request.getName(), userId);
        Board saved = boardRepository.save(board);
        return BoardDTO.BoardResponse.from(saved);
    }
}
