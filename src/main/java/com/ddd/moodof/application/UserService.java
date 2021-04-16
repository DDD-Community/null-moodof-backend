package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.UserDTO;
import com.ddd.moodof.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserDTO.UserResponse findById(Long id) {
        return UserDTO.UserResponse.from(userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 user")));
    }
}
