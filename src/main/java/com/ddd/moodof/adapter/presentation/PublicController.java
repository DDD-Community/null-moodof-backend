package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.PublicAPI;
import com.ddd.moodof.application.BoardPhotoService;
import com.ddd.moodof.application.StoragePhotoService;
import com.ddd.moodof.application.dto.BoardPhotoDTO;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(PublicController.API_PUBLIC)
@RestController
public class PublicController implements PublicAPI {

    public static final String API_PUBLIC = "/api/public";

    private final StoragePhotoService storagePhotoService;

    private final BoardPhotoService boardPhotoService;

    @Override
    @GetMapping("/boards/{sharedKey}")
    public ResponseEntity<List<BoardPhotoDTO.BoardPhotoResponse>> findAllByBoard(@PathVariable String sharedKey) {
        List<BoardPhotoDTO.BoardPhotoResponse> responses = boardPhotoService.findAllBySharedKey(sharedKey);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/boards/{sharedKey}/detail/{id}")
    public ResponseEntity<StoragePhotoDTO.StoragePhotoDetailResponse> getSharedBoardDetail(@PathVariable String sharedKey,@PathVariable Long id){
        StoragePhotoDTO.StoragePhotoDetailResponse response = storagePhotoService.findSharedBoardDetail(sharedKey, id);
        return ResponseEntity.ok(response);
    }
}
