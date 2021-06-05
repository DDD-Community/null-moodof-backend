package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.TrashPhotoAPI;
import com.ddd.moodof.application.TrashPhotoService;
import com.ddd.moodof.application.dto.TrashPhotoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(TrashPhotoController.API_TRASH_PHOTO)
@RestController
public class TrashPhotoController implements TrashPhotoAPI {
    public static final String API_TRASH_PHOTO = "/api/trash-photos";

    private final TrashPhotoService trashPhotoService;

    @Override
    @PostMapping
    public ResponseEntity<List<TrashPhotoDTO.TrashPhotoCreatedResponse>> add(@LoginUserId Long userId, @RequestBody TrashPhotoDTO.CreateTrashPhotos request) {
        List<TrashPhotoDTO.TrashPhotoCreatedResponse> responses = trashPhotoService.add(userId, request);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping
    public ResponseEntity<TrashPhotoDTO.TrashPhotoPageResponse> findPage(
            @LoginUserId Long userId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "lastModifiedDate") String sortBy,
            @RequestParam(defaultValue = "true") boolean descending) {
        TrashPhotoDTO.TrashPhotoPageResponse response = trashPhotoService.findPage(userId, page, size, sortBy, descending);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> delete(@LoginUserId Long userId, @RequestBody TrashPhotoDTO.TrashPhotosRequest request) {
        trashPhotoService.delete(request, userId);
        return ResponseEntity.noContent().build();
    }
}
