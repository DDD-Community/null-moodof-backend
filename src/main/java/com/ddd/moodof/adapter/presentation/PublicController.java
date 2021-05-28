package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.PublicAPI;
import com.ddd.moodof.application.BoardService;
import com.ddd.moodof.application.StoragePhotoService;
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

    private final BoardService boardService;

    @Override
    @GetMapping("/boards/{sharedId}")
    public ResponseEntity<List<CategoryDTO.CategoryWithBoardResponse>> getSharedBoard(@PathVariable String sharedId){
        List<CategoryDTO.CategoryWithBoardResponse> response = boardService.findBySharedKey(sharedId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/boards/{sharedId}/detail/{id}")
    public ResponseEntity<StoragePhotoDTO.StoragePhotoDetailResponse> getSharedBoardDetail(@PathVariable String sharedId,@PathVariable Long id){
        StoragePhotoDTO.StoragePhotoDetailResponse response = storagePhotoService.findSharedBoardDetail(sharedId, id);
        return ResponseEntity.ok(response);
    }
}
