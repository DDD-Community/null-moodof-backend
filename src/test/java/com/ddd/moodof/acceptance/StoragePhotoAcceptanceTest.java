package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import static com.ddd.moodof.adapter.presentation.StoragePhotoController.API_STORAGE_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StoragePhotoAcceptanceTest extends AcceptanceTest {
    private Long userId;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        User user = signUp();
        userId = user.getId();
    }

    @Test
    void 사진보관함에_사진을_추가한다() {
        // when
        String requestUri = "uri";
        String requestRepresentativeColor = "representativeColor";
        StoragePhotoDTO.StoragePhotoResponse response = 보관함사진_생성(userId, requestUri, requestRepresentativeColor);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getUri()).isEqualTo(requestUri),
                () -> assertThat(response.getRepresentativeColor()).isEqualTo(requestRepresentativeColor),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isEqualTo(response.getLastModifiedDate())
        );
    }

    @Test
    void 사진보관함_페이지_조회() {
        // given
        보관함사진_생성(userId, "1", "1");
        보관함사진_생성(userId, "2", "2");
        보관함사진_생성(userId, "3", "3");
        보관함사진_생성(userId, "4", "4");
        StoragePhotoDTO.StoragePhotoResponse top = 보관함사진_생성(userId, "5", "5");

        // when
        String uri = UriComponentsBuilder.fromUriString(API_STORAGE_PHOTO)
                .queryParam("page", 0)
                .queryParam("size", 3)
                .queryParam("sortBy", "lastModifiedDate")
                .queryParam("descending", "true")
                .build().toUriString();
        StoragePhotoDTO.StoragePhotoPageResponse response = getWithLogin(uri, StoragePhotoDTO.StoragePhotoPageResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(response.getStoragePhotos().size()).isEqualTo(3),
                () -> assertThat(response.getStoragePhotos().get(0)).usingRecursiveComparison().isEqualTo(top),
                () -> assertThat(response.getTotalPageCount()).isEqualTo(2)
        );
    }

    @Test
    void 보관함사진_삭제() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhotoResponse = 보관함사진_생성(userId, "uri", "color");

        // when then
        deleteWithLogin(API_STORAGE_PHOTO, storagePhotoResponse.getId(), userId);
    }
}
