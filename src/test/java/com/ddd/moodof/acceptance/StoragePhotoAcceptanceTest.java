package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.Test;

import static com.ddd.moodof.adapter.presentation.StoragePhotoController.API_STORAGE_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StoragePhotoAcceptanceTest extends AcceptanceTest {
    @Test
    void 사진보관함에_사진을_추가한다() {
        // given
        User user = signUp();
        Long userId = user.getId();

        // when
        StoragePhotoDTO.CreateStoragePhoto request = new StoragePhotoDTO.CreateStoragePhoto("uri", "representativeColor");
        StoragePhotoDTO.StoragePhotoResponse response = postWithLogin(request, API_STORAGE_PHOTO, StoragePhotoDTO.StoragePhotoResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getUri()).isEqualTo(request.getUri()),
                () -> assertThat(response.getRepresentativeColor()).isEqualTo(request.getRepresentativeColor()),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isEqualTo(response.getLastModifiedDate())
        );
    }
}
