package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.application.dto.TrashPhotoDTO;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

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
        TrashPhotoDTO.TrashPhotoResponse response = 보관함사진_휴지통_이동(storagePhoto.getId(), userId);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getStoragePhoto()).usingRecursiveComparison().isEqualTo(storagePhoto),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isEqualTo(response.getCreatedDate())
        );
    }

    @Test
    void 휴지통_목록_조회() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto1 = 보관함사진_생성(userId, "uri", "representativeColor");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto2 = 보관함사진_생성(userId, "uri", "representativeColor");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto3 = 보관함사진_생성(userId, "uri", "representativeColor");
        보관함사진_생성(userId, "uri", "representativeColor");
        보관함사진_휴지통_이동(storagePhoto1.getId(), userId);
        보관함사진_휴지통_이동(storagePhoto2.getId(), userId);
        TrashPhotoDTO.TrashPhotoResponse top = 보관함사진_휴지통_이동(storagePhoto3.getId(), userId);

        // when
        String uri = UriComponentsBuilder.fromUriString(API_TRASH_PHOTO)
                .queryParam("page", 0)
                .queryParam("size", 2)
                .queryParam("sortBy", "lastModifiedDate")
                .queryParam("descending", "true")
                .build().toUriString();
        TrashPhotoDTO.TrashPhotoPageResponse response = getWithLogin(uri, TrashPhotoDTO.TrashPhotoPageResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(response.getTotalPageCount()).isEqualTo(2),
                () -> assertThat(response.getResponses().get(0).getStoragePhoto()).usingRecursiveComparison().isEqualTo(storagePhoto3),
                () -> assertThat(response.getResponses().get(0).getId()).isEqualTo(top.getId())
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
