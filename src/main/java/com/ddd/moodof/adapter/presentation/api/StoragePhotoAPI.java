package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

public interface StoragePhotoAPI {
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<StoragePhotoDTO.StoragePhotoResponse> create(@RequestBody @Valid StoragePhotoDTO.CreateStoragePhoto request, @ApiIgnore @LoginUserId Long userId);
}
