package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.application.dto.TrashPhotoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.ddd.moodof.adapter.presentation.TrashPhotoController.API_TRASH_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TrashPhotoAcceptanceTest extends AcceptanceTest {
    @Test
    void 복수의_사진을_휴지통으로_이동한다() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "uri", "representativeColor");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto2 = 보관함사진_생성(userId, "uri", "representativeColor");

        // when
        List<TrashPhotoDTO.TrashPhotoCreatedResponse> responses = 보관함사진_휴지통_이동(List.of(storagePhoto.getId(), storagePhoto2.getId()), userId);

        // then
        assertAll(
                () -> assertThat(responses.get(0).getId()).isNotNull(),
                () -> assertThat(responses.get(0).getStoragePhotoId()).isEqualTo(storagePhoto.getId()),
                () -> assertThat(responses.get(0).getUserId()).isEqualTo(userId),
                () -> assertThat(responses.get(0).getCreatedDate()).isNotNull(),
                () -> assertThat(responses.get(0).getLastModifiedDate()).isEqualTo(responses.get(0).getCreatedDate()),
                () -> assertThat(responses.get(1).getId()).isNotNull(),
                () -> assertThat(responses.get(1).getStoragePhotoId()).isEqualTo(storagePhoto2.getId()),
                () -> assertThat(responses.get(1).getUserId()).isEqualTo(userId),
                () -> assertThat(responses.get(1).getCreatedDate()).isNotNull(),
                () -> assertThat(responses.get(1).getLastModifiedDate()).isEqualTo(responses.get(1).getCreatedDate())
        );
    }

    @Test
    void 휴지통_목록_조회() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto1 = 보관함사진_생성(userId, "uri", "representativeColor");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto2 = 보관함사진_생성(userId, "uri", "representativeColor");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto3 = 보관함사진_생성(userId, "uri", "representativeColor");
        보관함사진_생성(userId, "uri", "representativeColor");
        보관함사진_휴지통_이동(List.of(storagePhoto1.getId(), storagePhoto2.getId()), userId);
        List<TrashPhotoDTO.TrashPhotoCreatedResponse> top = 보관함사진_휴지통_이동(List.of(storagePhoto3.getId()), userId);

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
                () -> assertThat(response.getResponses().get(0).getId()).isEqualTo(top.get(0).getId())
        );
    }

    @Test
    void 휴지통의_사진을_복구한다() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "uri", "representativeColor");
        List<TrashPhotoDTO.TrashPhotoCreatedResponse> responses = 보관함사진_휴지통_이동(List.of(storagePhoto.getId()), userId);

        // when then
        deleteWithLogin(API_TRASH_PHOTO, responses.get(0).getId(), userId);
    }
}
