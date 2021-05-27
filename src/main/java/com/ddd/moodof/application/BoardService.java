package com.ddd.moodof.application;

import com.ddd.moodof.adapter.infrastructure.configuration.EncryptConfig;
import com.ddd.moodof.adapter.infrastructure.security.encrypt.EncryptUtil;
import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.application.dto.SharedDTO;
import com.ddd.moodof.application.verifier.BoardVerifier;
import com.ddd.moodof.domain.model.board.Board;
import com.ddd.moodof.domain.model.board.BoardSharedKeyUpdater;
import com.ddd.moodof.domain.model.board.BoardRepository;
import com.ddd.moodof.domain.model.board.BoardSequenceUpdater;
import com.ddd.moodof.domain.model.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.GeneralSecurityException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final BoardVerifier boardVerifier;

    private final BoardSequenceUpdater boardSequenceUpdater;

    private final BoardSharedKeyUpdater boardSharedKeyUpdater;

    private final EncryptConfig encryptConfig;

    public static final String LOCALHOST = "localhost";

    @Transactional
    public BoardDTO.BoardResponse create(Long userId, BoardDTO.CreateBoard request) {
        Board board = boardVerifier.toEntity(request.getPreviousBoardId(), request.getCategoryId(), request.getName(), userId);
        Board saved = boardRepository.save(board);

        boardRepository.findByPreviousBoardIdAndIdNot(request.getPreviousBoardId(), saved.getId())
                .ifPresent(it -> it.changePreviousBoardId(saved.getId(), userId));

        return BoardDTO.BoardResponse.from(saved);
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

    public SharedDTO.SharedBoardResponse createSharedKey(Long id, Long userId, HttpServletRequest httpServletRequest) {
        //todo 암호화된 값을 board로 등록시킨다
        String requestURL= getUrl(httpServletRequest);
        String sharedKey = null;
        try {
            sharedKey = EncryptUtil.encryptAES256(String.valueOf(id), encryptConfig.getKey());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Board, id = " + id));
        boardSharedKeyUpdater.update(board, sharedKey, userId);
        return createBoardsURL(id, requestURL, sharedKey);
    }

    public SharedDTO.SharedBoardResponse createBoardsURL(Long id, String requestURL, String sharedKey) {
        return generatedURL(id, requestURL, sharedKey);
    }
//    public List<CategoryDTO.CategoryResponse> findBySharedKey(String sharedKey) {
//        Board board = boardRepository.findBySharedKey(sharedKey)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 sharedKey : " + sharedKey));
////        List<Category> totalCategories = categoryRepository.findAllByUserId(board.getUserId());
////        return CategoryDTO.CategoryResponse.listForm(totalCategories);
//    }


    private SharedDTO.SharedBoardResponse generatedURL(Long id, String requestURL, String sharedKey){
        StringBuilder sharedURL = new StringBuilder();
        sharedURL.append(requestURL)
                .append("/")
                .append(sharedKey);
        return SharedDTO.SharedBoardResponse.from(id,sharedURL.toString(), sharedKey);
    }



    public String getUrl(HttpServletRequest request) {
        ServletServerHttpRequest httpRequest = new ServletServerHttpRequest(request);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();

        String scheme = uriComponents.getScheme();             // http / https
        String serverName = request.getServerName();     // hostname.com
        int serverPort = request.getServerPort();        // 80
        String contextPath = request.getContextPath();   // /app

        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://");
        url.append(serverName);

        if (serverName.equals(LOCALHOST)) {
            url.append(":").append(serverPort);
        }else {
            if (serverPort != 80 && serverPort != 443) {
                url.append(":").append(serverPort);
            }
        }
        url.append(contextPath);
        return url.toString();
    }
}
