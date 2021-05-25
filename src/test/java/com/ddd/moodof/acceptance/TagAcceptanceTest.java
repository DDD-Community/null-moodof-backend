package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.application.dto.TagDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ddd.moodof.adapter.presentation.TagController.API_TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TagAcceptanceTest extends AcceptanceTest {
    @Test
    public void 유저아이디_태그_전체_조회() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        태그_생성(userId, storagePhoto.getId(), "tag1");
        태그_생성(userId, storagePhoto.getId(), "tag2");
        태그_생성(userId, storagePhoto.getId(), "tag3");
        태그_생성(userId, storagePhoto.getId(), "tag4");

        // when
        String uri = API_TAG;
        List<TagDTO.TagResponse> response = getListWithLogin(uri, TagDTO.TagResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(response.size()).isEqualTo(4),
                () -> assertThat(response.get(0).getName()).isEqualTo("tag1")
        );
    }

    @Test
    public void 태그를_생성한다() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");

        // when
        TagDTO.TagCreatedResponse response = 태그_생성(userId, storagePhoto.getId(), "name1");

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getName()).isEqualTo("name1"),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isNotNull(),
                () -> assertThat(response.getTagAttachment().getId()).isNotNull(),
                () -> assertThat(response.getTagAttachment().getStoragePhotoId()).isEqualTo(storagePhoto.getId()),
                () -> assertThat(response.getTagAttachment().getTagId()).isEqualTo(response.getId())
        );
    }

    @Test
    public void 태그를_삭제한다() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        태그_생성(userId, storagePhoto.getId(), "name1");
        TagDTO.TagCreatedResponse response = 태그_생성(userId, storagePhoto.getId(), "name2");

        // when
        deleteWithLogin(API_TAG, response.getId(), userId);
    }

    @Test
    public void 태그_수정() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        TagDTO.TagCreatedResponse tag = 태그_생성(userId, storagePhoto.getId(), "name1");

        // when
        TagDTO.TagResponse updateList = 태그_수정(tag.getId(), userId, "name2");

        // then
        assertThat(updateList.getName()).isEqualTo("name2");
    }
}
