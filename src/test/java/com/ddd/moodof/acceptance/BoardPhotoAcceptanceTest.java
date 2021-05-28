package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.BoardPhotoDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.ddd.moodof.adapter.presentation.BoardPhotoController.API_BOARD_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardPhotoAcceptanceTest extends AcceptanceTest {
    private StoragePhotoDTO.StoragePhotoResponse storagePhoto;
    private StoragePhotoDTO.StoragePhotoResponse storagePhoto2;
    private BoardDTO.BoardResponse board;
    private CategoryDTO.CategoryResponse category;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        storagePhoto2 = 보관함사진_생성(userId, "photoUri2", "representativeColor2");
        category = 카테고리_생성(userId, "title", 0L);
        board = 보드_생성(userId, 0L, category.getId(), "name");
    }

    @Test
    void 보드에_보관함사진을_추가한다() {
        // when
        List<BoardPhotoDTO.BoardPhotoResponse> responses = 보드_사진_복수_생성(userId, List.of(storagePhoto.getId(), storagePhoto2.getId()), board.getId());
        List<BoardPhotoDTO.BoardPhotoResponse> responses2 = 보드_사진_복수_생성(userId, List.of(storagePhoto.getId(), storagePhoto2.getId()), board.getId());

        // then
        assertAll(
                () -> assertThat(responses.size()).isEqualTo(2),
                () -> assertThat(responses.get(0).getPreviousBoardPhotoId()).isEqualTo(0L),
                () -> assertThat(responses.get(1).getPreviousBoardPhotoId()).isEqualTo(responses.get(0).getId()),
                () -> assertThat(responses2.get(0).getPreviousBoardPhotoId()).isEqualTo(responses.get(1).getId()),
                () -> assertThat(responses2.get(1).getPreviousBoardPhotoId()).isEqualTo(responses2.get(0).getId())
        );
    }

    @Test
    void 보드사진을_조회한다() {
        // given
        보드_사진_복수_생성(userId, List.of(storagePhoto.getId(), storagePhoto2.getId()), board.getId());

        // when
        List<BoardPhotoDTO.BoardPhotoResponse> responses = getListWithLogin(API_BOARD_PHOTO + "?boardId=" + board.getId(), BoardPhotoDTO.BoardPhotoResponse.class, userId);

        // then
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void 보드에서_보관함사진을_제거한다() {
        // given
        List<BoardPhotoDTO.BoardPhotoResponse> responses = 보드_사진_복수_생성(userId, List.of(storagePhoto.getId()), board.getId());

        // when then
        List<Long> boardPhotoIds = responses.stream()
                .map(BoardPhotoDTO.BoardPhotoResponse::getId)
                .collect(Collectors.toList());
        deleteListWithLogin(API_BOARD_PHOTO, new BoardPhotoDTO.RemoveBoardPhotos(boardPhotoIds), userId);
    }
}
