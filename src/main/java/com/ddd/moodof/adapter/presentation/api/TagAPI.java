package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.TagDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

public interface TagAPI {

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping
    ResponseEntity<List<TagDTO.TagResponse>> findAllByTag(@LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<TagDTO.TagResponse> create(@RequestBody @Valid TagDTO.CreateRequest request, @ApiIgnore @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping
    ResponseEntity<Void> delete(Long id, @ApiIgnore @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping
    ResponseEntity<TagDTO.TagResponse> update(@RequestBody @Valid TagDTO.UpdateReqeust request, @ApiIgnore @LoginUserId Long userId);
}
