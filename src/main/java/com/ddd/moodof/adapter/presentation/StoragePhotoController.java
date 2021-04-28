package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.StoragePhotoAPI;
import com.ddd.moodof.application.StoragePhotoService;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping(StoragePhotoController.API_STORAGE_PHOTO)
@RestController
public class StoragePhotoController implements StoragePhotoAPI {
    public static final String API_STORAGE_PHOTO = "/api/storage-photos";

    private final StoragePhotoService storagePhotoService;

    @Override
    @PostMapping
    public ResponseEntity<StoragePhotoDTO.StoragePhotoResponse> create(
            @RequestBody @Valid StoragePhotoDTO.CreateStoragePhoto request,
            @LoginUserId Long userId) {

        StoragePhotoDTO.StoragePhotoResponse response = storagePhotoService.create(request, userId);
        return ResponseEntity.created(URI.create(API_STORAGE_PHOTO + "/" + response.getId())).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<StoragePhotoDTO.StoragePhotoPageResponse> findPage(
            @LoginUserId Long userId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "lastModifiedDate") String sortBy,
            @RequestParam(defaultValue = "true") boolean descending) {

        StoragePhotoDTO.StoragePhotoPageResponse response = storagePhotoService.findPage(userId, page, size, sortBy, descending);
        return ResponseEntity.ok(response);

    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@LoginUserId Long userId, @PathVariable Long id) {
        storagePhotoService.deleteById(userId, id);
        return ResponseEntity.noContent().build();
    }
}
