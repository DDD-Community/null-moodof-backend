package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.BoardPhotoAPI;
import com.ddd.moodof.application.BoardPhotoService;
import com.ddd.moodof.application.dto.BoardPhotoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping(BoardPhotoController.API_BOARD_PHOTO)
@RestController
public class BoardPhotoController implements BoardPhotoAPI {
    public static final String API_BOARD_PHOTO = "/api/board-photos";

    private final BoardPhotoService boardPhotoService;

    @Override
    @PostMapping
    public ResponseEntity<List<BoardPhotoDTO.BoardPhotoResponse>> addPhotos(@LoginUserId Long userId, @RequestBody BoardPhotoDTO.AddBoardPhoto request) {
        List<BoardPhotoDTO.BoardPhotoResponse> responses = boardPhotoService.addPhotos(userId, request);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<BoardPhotoDTO.BoardPhotoResponse>> findAllByBoard(@LoginUserId Long userId, @RequestParam Long boardId) {
        List<BoardPhotoDTO.BoardPhotoResponse> responses = boardPhotoService.findAllByBoardId(boardId, userId);
        return ResponseEntity.ok(responses);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> removePhoto(@LoginUserId Long userId, @RequestBody BoardPhotoDTO.RemoveBoardPhotos request) {
        boardPhotoService.removePhoto(userId, request);
        return ResponseEntity.noContent().build();
    }
}