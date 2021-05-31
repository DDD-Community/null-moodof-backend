package com.ddd.moodof.application;

import com.ddd.moodof.adapter.infrastructure.security.encrypt.EncryptUtil;
import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.verifier.BoardVerifier;
import com.ddd.moodof.domain.model.board.Board;
import com.ddd.moodof.domain.model.board.BoardRepository;
import com.ddd.moodof.domain.model.board.BoardSequenceUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {
    public static final int MAX_BOARD_IN_CATEGORY_COUNT = 10;

    private final BoardRepository boardRepository;

    private final BoardVerifier boardVerifier;

    private final BoardSequenceUpdater boardSequenceUpdater;

    public static final String LOCALHOST = "localhost";


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
        boardRepository.save(saved);
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

    public BoardDTO.BoardSharedResponse getSharedURI(Long userId, Long id, HttpServletRequest httpServletRequest) {
        Board board = boardRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Board, id = " + id));
        String requestURI = getUrl(httpServletRequest);
        String sharedKey = board.getSharedKey();
        String sharedURI = generatedURI(requestURI, sharedKey);
        return BoardDTO.BoardSharedResponse.from(id,sharedURI, sharedKey);
    }

    public String getUrl(HttpServletRequest request) {
        ServletServerHttpRequest httpRequest = new ServletServerHttpRequest(request);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();

        String scheme = uriComponents.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getRequestURI();
        StringBuilder url = new StringBuilder();

        url.append(scheme).append("://");
        url.append(serverName);

        if (serverName.equals(LOCALHOST)) {
            url.append(":").append("8080");
        }else {
            if (serverPort != 80 && serverPort != 443) {
                url.append(":").append(serverPort);
            }
        }
        url.append(contextPath);
        return url.toString();
    }

    private String generatedURI(String requestURL, String sharedKey) {
        return UriComponentsBuilder.fromUriString(requestURL)
                .path(sharedKey)
                .build()
                .toUriString();
    }
}
