package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.PublicBoardAPI;
import com.ddd.moodof.application.BoardPhotoService;
import com.ddd.moodof.application.StoragePhotoService;
import com.ddd.moodof.application.dto.BoardPhotoDTO;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(PublicBoardController.API_PUBLIC_BOARDS)
@RestController
public class PublicBoardController implements PublicBoardAPI {

    public static final String API_PUBLIC_BOARDS = "/api/public/boards";

    private final StoragePhotoService storagePhotoService;

    private final BoardPhotoService boardPhotoService;

    @Override
    @GetMapping("/{sharedKey}")
    public ResponseEntity<List<BoardPhotoDTO.BoardPhotoResponse>> findAllByBoard(@PathVariable String sharedKey) {
        List<BoardPhotoDTO.BoardPhotoResponse> responses = boardPhotoService.findAllBySharedKey(sharedKey);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{sharedKey}/detail/{id}")
    public ResponseEntity<StoragePhotoDTO.StoragePhotoDetailResponse> getSharedBoardDetail(
            @PathVariable String sharedKey,
            @PathVariable Long id,
            @RequestParam(required = false, value = "tagIds") List<Long> tagIds){
        StoragePhotoDTO.StoragePhotoDetailResponse response = storagePhotoService.findSharedBoardDetail(sharedKey, id, tagIds);
        return ResponseEntity.ok(response);
    }
}
