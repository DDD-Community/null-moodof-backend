package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.application.dto.TagAttachmentDTO;
import com.ddd.moodof.application.dto.TagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ddd.moodof.adapter.presentation.TagAttachmentController.API_TAG_ATTACHMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TagAttachmentAcceptanceTest extends AcceptanceTest {
    private StoragePhotoDTO.StoragePhotoResponse storagePhoto;
    private TagDTO.TagCreatedResponse tag;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        tag = 태그_생성(userId, storagePhoto.getId(), "tag");
    }

    @Test
    void 보관함사진에_태그를_붙인다() {
        // when
        TagAttachmentDTO.TagAttachmentResponse response = 태그붙이기_생성(userId, storagePhoto.getId(), tag.getId());

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getStoragePhotoId()).isEqualTo(storagePhoto.getId()),
                () -> assertThat(response.getTagId()).isEqualTo(tag.getId()),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isEqualTo(response.getCreatedDate())
        );
    }

    @Test
    void 보관함사진에_태그를_뗀다() {
        // given
        TagAttachmentDTO.TagAttachmentResponse response = 태그붙이기_생성(userId, storagePhoto.getId(), tag.getId());

        // when then
        deleteWithLogin(API_TAG_ATTACHMENT, response.getId(), userId);
    }
}
