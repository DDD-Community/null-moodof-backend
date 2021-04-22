package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

public interface StoragePhotoAPI {
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<StoragePhotoDTO.StoragePhotoResponse> create(@RequestBody @Valid StoragePhotoDTO.CreateStoragePhoto request, @ApiIgnore @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping
    ResponseEntity<List<StoragePhotoDTO.StoragePhotoResponse>> findPage(@ApiIgnore Long userId, @ApiParam(value = "Starts at 0") @RequestParam int page, @RequestParam int size, @RequestParam String sortBy, @RequestParam boolean descending);
}
