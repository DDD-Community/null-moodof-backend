package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.BoardPhotoDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ddd.moodof.adapter.presentation.BoardPhotoController.API_BOARD_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardPhotoAcceptanceTest extends AcceptanceTest {
    private StoragePhotoDTO.StoragePhotoResponse storagePhoto;
    private BoardDTO.BoardResponse board;
    private CategoryDTO.CategoryResponse category;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        category = 카테고리_생성(userId, "title", 0L);
        board = 보드_생성(userId, 0L, category.getId(), "name");
    }

    @Test
    void 보드에_보관함사진을_추가한다() {
        // when
        BoardPhotoDTO.BoardPhotoResponse response = 보드_사진_생성(userId, storagePhoto.getId(), board.getId());

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getStoragePhotoId()).isEqualTo(storagePhoto.getId()),
                () -> assertThat(response.getBoardId()).isEqualTo(board.getId()),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getLastModifiedDate()).isEqualTo(response.getCreatedDate())
        );
    }

    @Test
    void 보드에서_보관함사진을_제거한다() {
        // given
        BoardPhotoDTO.BoardPhotoResponse response = 보드_사진_생성(userId, storagePhoto.getId(), board.getId());

        // when then
        deleteWithLogin(API_BOARD_PHOTO, response.getId(), userId);
    }
}
