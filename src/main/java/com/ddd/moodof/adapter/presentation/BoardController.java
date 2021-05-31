package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.BoardAPI;
import com.ddd.moodof.application.BoardService;
import com.ddd.moodof.application.dto.BoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping(BoardController.API_BOARD)
@RestController
public class BoardController implements BoardAPI {
    public static final String API_BOARD = "/api/boards";

    private final BoardService boardService;

    @Override
    @PostMapping
    public ResponseEntity<BoardDTO.BoardResponse> create(@LoginUserId Long userId, @Valid @RequestBody BoardDTO.CreateBoard request) {
        BoardDTO.BoardResponse response = boardService.create(userId, request);
        return ResponseEntity.created(URI.create(API_BOARD + "/" + response.getId())).body(response);
    }

    @Override
    @PutMapping("/{id}/name")
    public ResponseEntity<BoardDTO.BoardResponse> changeName(@LoginUserId Long userId, @PathVariable Long id, @RequestBody BoardDTO.ChangeBoardName request) {
        BoardDTO.BoardResponse response = boardService.changeName(userId, id, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/{id}/previousBoardId")
    public ResponseEntity<BoardDTO.BoardResponse> changeSequence(@LoginUserId Long userId, @PathVariable Long id, @RequestBody BoardDTO.ChangeBoardSequence request) {
        BoardDTO.BoardResponse response = boardService.updateSequence(userId, id, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<BoardDTO.BoardResponse> delete(@LoginUserId Long userId, @PathVariable Long id) {
        boardService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO.BoardSharedResponse> getSharedKey(@LoginUserId Long userId, @PathVariable Long id, HttpServletRequest httpServletRequest){
        BoardDTO.BoardSharedResponse response = boardService.getSharedURI(userId, id, httpServletRequest);
        return ResponseEntity.ok(response);
    }
}
