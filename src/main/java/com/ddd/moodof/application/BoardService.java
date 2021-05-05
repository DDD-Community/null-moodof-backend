package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.domain.model.board.Board;
import com.ddd.moodof.domain.model.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public BoardDTO.BoardResponse create(Long userId, BoardDTO.CreateBoard request) {
        // TODO: 2021/05/05 verify category
        Board saved = boardRepository.save(request.toEntity(userId));

        boardRepository.findAllByPreviousBoardIdAndUserId(request.getPreviousBoardId(), userId)
                .stream()
                .filter(it -> it.isNotEqual(saved.getId()))
                .findAny()
                .ifPresent(it -> it.updatePreviousBoardId(saved.getId()));

        return BoardDTO.BoardResponse.from(saved);
    }
}
