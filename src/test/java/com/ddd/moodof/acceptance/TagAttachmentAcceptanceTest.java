package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.application.dto.TagAttachmentDTO;
import com.ddd.moodof.application.dto.TagDTO;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ddd.moodof.adapter.presentation.TagAttachmentController.API_TAG_ATTACHMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TagAttachmentAcceptanceTest extends AcceptanceTest {
    private Long userId;
    private StoragePhotoDTO.StoragePhotoResponse storagePhoto;
    private TagDTO.TagResponse tag;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        User user = signUp();
        userId = user.getId();
        storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        tag = 태그_생성(userId, "tag");
    }

    @Test
    void 보관함사진에_태그를_붙인다() {
        // when
        TagAttachmentDTO.CreateTagAttachment request = new TagAttachmentDTO.CreateTagAttachment(storagePhoto.getId(), tag.getId());
        TagAttachmentDTO.TagAttachmentResponse response = postWithLogin(request, API_TAG_ATTACHMENT, TagAttachmentDTO.TagAttachmentResponse.class, userId);

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
}
