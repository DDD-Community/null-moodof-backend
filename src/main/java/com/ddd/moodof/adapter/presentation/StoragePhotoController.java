package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.StoragePhotoAPI;
import com.ddd.moodof.application.StoragePhotoService;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<StoragePhotoDTO.StoragePhotoResponse> create(@RequestBody @Valid StoragePhotoDTO.CreateStoragePhoto request, @LoginUserId Long userId) {
        StoragePhotoDTO.StoragePhotoResponse response = storagePhotoService.create(request, userId);
        return ResponseEntity.created(URI.create(API_STORAGE_PHOTO + "/" + response.getId())).body(response);
    }
}
