package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.UserDTO;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserAcceptanceTest extends AcceptanceTest {

    @Test
    void 자신의_정보를_조회한다() {
        // given
        User user = signUp();

        // when
        UserDTO.UserResponse response = getWithLogin("/api/me", UserDTO.UserResponse.class, user.getId());

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(user.getId()),
                () -> assertThat(response.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(response.getNickname()).isEqualTo(user.getNickname()),
                () -> assertThat(response.getProfileUrl()).isEqualTo(user.getProfileUrl()),
                () -> assertThat(response.getCreatedDate()).isEqualTo(user.getCreatedDate()),
                () -> assertThat(response.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate())
        );
    }
}
