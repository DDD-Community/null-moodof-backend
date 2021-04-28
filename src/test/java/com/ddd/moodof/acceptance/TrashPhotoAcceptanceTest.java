package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.application.dto.TrashPhotoDTO;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ddd.moodof.adapter.presentation.TrashPhotoController.API_TRASH_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TrashPhotoAcceptanceTest extends AcceptanceTest {
    private Long userId;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        User user = signUp();
        userId = user.getId();
    }

    @Test
    void 사진을_휴지통으로_이동한다() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "uri", "representativeColor");

        // when
        TrashPhotoDTO.TrashPhotoResponse response = postWithLogin(new TrashPhotoDTO.CreateTrashPhoto(storagePhoto.getId()), API_TRASH_PHOTO, TrashPhotoDTO.TrashPhotoResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getStoragePhotoId()).isEqualTo(storagePhoto.getId()),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isEqualTo(response.getCreatedDate())
        );
    }

    @Test
    void 휴지통의_사진을_복구한다() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "uri", "representativeColor");
        TrashPhotoDTO.TrashPhotoResponse response = postWithLogin(new TrashPhotoDTO.CreateTrashPhoto(storagePhoto.getId()), API_TRASH_PHOTO, TrashPhotoDTO.TrashPhotoResponse.class, userId);

        // when then
        deleteWithLogin(API_TRASH_PHOTO, response.getId(), userId);
    }
}
