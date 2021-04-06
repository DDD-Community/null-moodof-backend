package com.ddd.moodof.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(StoragePhotoController.API_STORAGE_PHOTO)
@RestController
public class StoragePhotoController {
    public static final String API_STORAGE_PHOTO = "/api/storage-photos";
}
