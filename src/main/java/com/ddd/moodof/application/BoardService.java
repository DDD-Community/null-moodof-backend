package com.ddd.moodof.application;

import com.ddd.moodof.adapter.infrastructure.security.encrypt.EncryptUtil;
import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.verifier.BoardVerifier;
import com.ddd.moodof.domain.model.board.Board;
import com.ddd.moodof.domain.model.board.BoardRepository;
import com.ddd.moodof.domain.model.board.BoardSequenceUpdater;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {
  
    public static final int MAX_BOARD_IN_CATEGORY_COUNT = 10;
  
    private final BoardRepository boardRepository;

    private final BoardVerifier boardVerifier;

    private final BoardSequenceUpdater boardSequenceUpdater;

    @Transactional
    public BoardDTO.BoardResponse create(Long userId, BoardDTO.CreateBoard request) {
        if (boardRepository.countByCategoryId(request.getCategoryId()) >= MAX_BOARD_IN_CATEGORY_COUNT) {
            throw new IllegalStateException("하나의 카테고리에 생성할 수 있는 보드 최대 개수는 10개 입니다.");
        }

        Board board = boardVerifier.toEntity(request.getPreviousBoardId(), request.getCategoryId(), request.getName(), userId);
        Board saved = boardRepository.save(board);
        encryptByBoardId(userId, saved);
        boardRepository.findByUserIdAndPreviousBoardIdAndIdNot(userId, request.getPreviousBoardId(), saved.getId())
                .ifPresent(it -> it.changePreviousBoardId(saved.getId(), userId));
        return BoardDTO.BoardResponse.from(saved);
    }

    public void encryptByBoardId(Long userId, Board saved) {
        String sharedKey = EncryptUtil.encryptSHA256(Long.toString(saved.getId()));
        saved.updateSharedkey(sharedKey, userId);
    }

    public BoardDTO.BoardResponse changeName(Long userId, Long id, BoardDTO.ChangeBoardName request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보드입니다. id = " + id));

        board.changeName(request.getName(), userId);
        Board saved = boardRepository.save(board);
        return BoardDTO.BoardResponse.from(saved);
    }

    public BoardDTO.BoardResponse updateSequence(Long userId, Long id, BoardDTO.ChangeBoardSequence request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Board, id = " + id));
        Board updated = boardSequenceUpdater.update(board, request.getPreviousBoardId(), request.getCategoryId(), userId);
        return BoardDTO.BoardResponse.from(updated);
    }

    public void delete(Long userId, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Board, id = " + id));
        if (board.isUserNotEqual(userId)) {
            throw new IllegalArgumentException("userId가 일치하지 않습니다. userId = " + userId);
        }
        boardRepository.deleteById(id);
    }

    public BoardDTO.BoardSharedResponse getSharedURI(Long userId, Long id) {
        Board board = boardRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Board, id = " + id));
        String sharedKey = board.getSharedKey();
        return BoardDTO.BoardSharedResponse.from(id, sharedKey);
    }

}
