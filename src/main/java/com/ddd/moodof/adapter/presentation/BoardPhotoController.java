package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.BoardPhotoAPI;
import com.ddd.moodof.application.BoardPhotoService;
import com.ddd.moodof.application.dto.BoardPhotoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RequiredArgsConstructor
@RequestMapping(BoardPhotoController.API_BOARD_PHOTO)
@RestController
public class BoardPhotoController implements BoardPhotoAPI {
    public static final String API_BOARD_PHOTO = "/api/board-photos";

    private final BoardPhotoService boardPhotoService;

    @Override
    @PostMapping
    public ResponseEntity<BoardPhotoDTO.BoardPhotoResponse> addPhoto(@LoginUserId Long userId, @RequestBody BoardPhotoDTO.AddBoardPhoto request) {
        BoardPhotoDTO.BoardPhotoResponse response = boardPhotoService.addPhoto(userId, request);
        return ResponseEntity.created(URI.create(API_BOARD_PHOTO + "/" + response.getId())).body(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePhoto(@LoginUserId Long userId, @PathVariable Long id) {
        boardPhotoService.removePhoto(userId, id);
        return ResponseEntity.noContent().build();
    }
}