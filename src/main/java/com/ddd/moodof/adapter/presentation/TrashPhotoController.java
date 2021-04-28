package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.TrashPhotoAPI;
import com.ddd.moodof.application.TrashPhotoService;
import com.ddd.moodof.application.dto.TrashPhotoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping(TrashPhotoController.API_TRASH_PHOTO)
@RestController
public class TrashPhotoController implements TrashPhotoAPI {
    public static final String API_TRASH_PHOTO = "/api/trash-photos";

    private final TrashPhotoService trashPhotoService;

    @Override
    @PostMapping
    public ResponseEntity<TrashPhotoDTO.TrashPhotoResponse> add(@LoginUserId Long userId, @RequestBody TrashPhotoDTO.CreateTrashPhoto request) {
        TrashPhotoDTO.TrashPhotoResponse response = trashPhotoService.add(userId, request);
        return ResponseEntity.created(URI.create(API_TRASH_PHOTO + "/" + response.getId())).body(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@LoginUserId Long userId, @PathVariable Long id) {
        trashPhotoService.cancel(id, userId);
        return ResponseEntity.noContent().build();
    }
}
