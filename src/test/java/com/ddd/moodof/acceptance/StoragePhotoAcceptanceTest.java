package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import org.junit.jupiter.api.Test;

import static com.ddd.moodof.presentation.StoragePhotoController.API_STORAGE_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StoragePhotoAcceptanceTest extends AcceptanceTest {
    @Test
    void 사진보관함에_사진을_추가한다() {
        // TODO: 2021/04/06 인증 인가 완료시 회원 가입 후 userId를 응답받아야 한다.
        // given
        Long userId = 1L;

        // when
        StoragePhotoDTO.Create request = new StoragePhotoDTO.Create("uri", "representativeColor");
        StoragePhotoDTO.Response response = postWithLogin(request, API_STORAGE_PHOTO, StoragePhotoDTO.Response.class, userId);

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
