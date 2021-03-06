package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.TrashPhotoDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

public interface TrashPhotoAPI {
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<List<TrashPhotoDTO.TrashPhotoCreatedResponse>> add(@ApiIgnore @LoginUserId Long userId, @RequestBody TrashPhotoDTO.CreateTrashPhotos request);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping
    ResponseEntity<TrashPhotoDTO.TrashPhotoPageResponse> findPage(
            @ApiIgnore @LoginUserId Long userId,
            @ApiParam(value = "Starts at 0") @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "lastModifiedDate") String sortBy,
            @RequestParam(defaultValue = "true") boolean descending);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping
    ResponseEntity<Void> cancel(@ApiIgnore @LoginUserId Long userId, @RequestBody TrashPhotoDTO.CancelTrashPhotos request);
}
