package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.SharedDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

public interface BoardAPI {
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<BoardDTO.BoardResponse> create(@ApiIgnore @LoginUserId Long userId, @RequestBody BoardDTO.CreateBoard request);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/{id}/name")
    ResponseEntity<BoardDTO.BoardResponse> changeName(@ApiIgnore @LoginUserId Long userId, @PathVariable Long id, @RequestBody BoardDTO.ChangeBoardName request);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/{id}/previousBoardId")
    ResponseEntity<BoardDTO.BoardResponse> changeSequence(@ApiIgnore @LoginUserId Long userId, @PathVariable Long id, @RequestBody BoardDTO.ChangeBoardSequence request);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping("/{id}")
    ResponseEntity<BoardDTO.BoardResponse> delete(@ApiIgnore @LoginUserId Long userId, @PathVariable Long id);

}
