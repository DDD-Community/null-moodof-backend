package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

public interface StoragePhotoAPI {
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<StoragePhotoDTO.StoragePhotoResponse> create(@RequestBody @Valid StoragePhotoDTO.CreateStoragePhoto request, @ApiIgnore @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping
    ResponseEntity<StoragePhotoDTO.StoragePhotoPageResponse> findPage(
            @ApiIgnore Long userId,
            @ApiParam(value = "Starts at 0") @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam boolean descending,
            @RequestParam(required = false, value = "tagIds[]") List<Long> tagIds);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/{id}")
    ResponseEntity<StoragePhotoDTO.StoragePhotoDetailResponse> findDetail(@ApiIgnore @LoginUserId Long userId, @PathVariable Long id);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@ApiIgnore Long userId, @PathVariable Long id);
}
