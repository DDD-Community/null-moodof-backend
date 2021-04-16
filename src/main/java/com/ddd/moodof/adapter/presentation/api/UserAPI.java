package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.UserDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

public interface UserAPI {
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/api/me")
    ResponseEntity<UserDTO.UserResponse> findMe(@ApiIgnore @LoginUserId Long id);
}
