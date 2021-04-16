package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.UserAPI;
import com.ddd.moodof.application.UserService;
import com.ddd.moodof.application.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserAPI {

    private final UserService userService;

    @Override
    @GetMapping("/api/me")
    public ResponseEntity<UserDTO.UserResponse> findMe(@LoginUserId Long id) {
        UserDTO.UserResponse response = userService.findById(id);
        return ResponseEntity.ok(response);
    }
}
