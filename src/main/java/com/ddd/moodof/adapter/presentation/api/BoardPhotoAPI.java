package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.BoardPhotoDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

public interface BoardPhotoAPI {
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<List<BoardPhotoDTO.BoardPhotoResponse>> addPhotos(@ApiIgnore Long userId, @RequestBody BoardPhotoDTO.AddBoardPhoto request);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping
    ResponseEntity<List<BoardPhotoDTO.BoardPhotoResponse>> findAllByBoard(@ApiIgnore @LoginUserId Long userId, @RequestParam Long boardId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping
    ResponseEntity<Void> removePhoto(@ApiIgnore @LoginUserId Long userId, @RequestBody BoardPhotoDTO.RemoveBoardPhotos request);
}
