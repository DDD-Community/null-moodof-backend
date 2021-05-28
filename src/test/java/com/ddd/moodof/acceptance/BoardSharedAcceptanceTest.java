package com.ddd.moodof.acceptance;

import com.ddd.moodof.adapter.infrastructure.configuration.EncryptConfig;
import com.ddd.moodof.adapter.infrastructure.security.encrypt.EncryptUtil;
import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.application.dto.TagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

import static com.ddd.moodof.adapter.presentation.PublicController.API_PUBLIC;
import static com.ddd.moodof.adapter.presentation.StoragePhotoController.API_STORAGE_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardSharedAcceptanceTest extends AcceptanceTest{
    private StoragePhotoDTO.StoragePhotoResponse storagePhoto;
    private BoardDTO.BoardResponse board;
    private CategoryDTO.CategoryResponse category;
    private CategoryDTO.CategoryResponse category2;

    @Autowired
    private EncryptConfig encryptConfig;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        category = 카테고리_생성(userId, "title", 0L);
        category2 = 카테고리_생성(userId, "title", category.getId());
        board = 보드_생성(userId, 0L, category.getId(), "name");
    }
    @Test
    public void 공유_URL_생성() throws Exception {
        // given

        // when
        BoardDTO.BoardSharedResponse response = 보드_공유하기_생성(board.getId(), userId);
        Long responseDecrypt = Long.parseLong(EncryptUtil.decryptAES256(response.getSharedKey(), encryptConfig.getKey()));

        // then
        assertAll(
                () -> assertThat(board.getCategoryId()).isEqualTo(category.getId()),
                () -> assertThat(response.getId()).isEqualTo(board.getId()),
                () -> assertThat(responseDecrypt).isEqualTo(board.getId())
        );
    }

    @Test
    public void 공유_카테고리_보드_리스트_조회() throws Exception {
        // given
        BoardDTO.BoardSharedResponse shared = 보드_공유하기_생성(board.getId(), userId);

        // when
        List<CategoryDTO.CategoryWithBoardResponse> response = getListNotLoginWithProperty(API_PUBLIC + "/boards", CategoryDTO.CategoryWithBoardResponse.class, shared.getSharedKey());

        // then
        assertAll(
                () -> assertThat(response.get(0).getBoardList().get(0).getId()).isEqualTo(board.getId()),
                () -> assertThat(response.get(0).getBoardList().get(0).getCategoryId()).isEqualTo(board.getCategoryId()),
                () -> assertThat(response.get(0).getBoardList().get(0).getUserId()).isEqualTo(board.getUserId()),
                () -> assertThat(response.get(0).getBoardList().get(0).getName()).isEqualTo(board.getName()),
                () -> assertThat(response.get(0).getBoardList().get(0).getPreviousBoardId()).isEqualTo(board.getPreviousBoardId())

        );
    }

    @Test
    void 사진보관함_페이지_조회() {
        // given
        보관함사진_생성(userId, "1", "1");
        보관함사진_생성(userId, "2", "2");
        StoragePhotoDTO.StoragePhotoResponse second = 보관함사진_생성(userId, "3", "3");
        StoragePhotoDTO.StoragePhotoResponse trash = 보관함사진_생성(userId, "4", "4");
        StoragePhotoDTO.StoragePhotoResponse top = 보관함사진_생성(userId, "5", "5");
        보관함사진_휴지통_이동(Collections.singletonList(trash.getId()), userId);

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
                () -> assertThat(response.getStoragePhotos().get(1)).usingRecursiveComparison().isEqualTo(second),
                () -> assertThat(response.getTotalPageCount()).isEqualTo(2)
        );
    }

    @Test
    void 공유_보관함사진_상세_조회() {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto1 = 보관함사진_생성(userId, "1", "1");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto2 = 보관함사진_생성(userId, "2", "2");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto3 = 보관함사진_생성(userId, "3", "3");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto4 = 보관함사진_생성(userId, "4", "4");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto5 = 보관함사진_생성(userId, "5", "5");

        BoardDTO.BoardResponse board1 = 보드_생성(userId, 0L, category.getId(), "board-1");
        BoardDTO.BoardResponse board2 = 보드_생성(userId, board1.getId(), category.getId(), "board-2");
        BoardDTO.BoardResponse board3 = 보드_생성(userId, 0L, category2.getId(), "board-3");
        보드_사진_생성(userId, storagePhoto1.getId(), board1.getId());
        보드_사진_생성(userId, storagePhoto2.getId(), board1.getId());
        보드_사진_생성(userId, storagePhoto2.getId(), board2.getId());
        보드_사진_생성(userId, storagePhoto2.getId(), board3.getId());
        보드_사진_생성(userId, storagePhoto3.getId(), board1.getId());
        보드_사진_생성(userId, storagePhoto4.getId(), board2.getId());
        TagDTO.TagResponse tag1 = 태그_생성(userId, "tag-1");
        TagDTO.TagResponse tag2 = 태그_생성(userId, "tag-2");
        태그붙이기_생성(userId, storagePhoto2.getId(), tag1.getId());
        태그붙이기_생성(userId, storagePhoto2.getId(), tag2.getId());
        보관함사진_휴지통_이동(List.of(storagePhoto3.getId(), storagePhoto4.getId()), userId);
        BoardDTO.BoardSharedResponse shared = 보드_공유하기_생성(board2.getId(), userId);

        // when
        StoragePhotoDTO.StoragePhotoDetailResponse response = getNotLoginWithMultiProperty(API_PUBLIC + "/boards/{property1}/detail/{property2}", StoragePhotoDTO.StoragePhotoDetailResponse.class, shared.getSharedKey(), storagePhoto2.getId());

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(storagePhoto2.getId()),
                () -> assertThat(response.getCreatedDate()).isEqualTo(storagePhoto2.getCreatedDate()),
                () -> assertThat(response.getLastModifiedDate()).isEqualTo(storagePhoto2.getLastModifiedDate()),
                () -> assertThat(response.getPreviousStoragePhotoId()).isEqualTo(storagePhoto5.getId()),
                () -> assertThat(response.getNextStoragePhotoId()).isEqualTo(storagePhoto1.getId()),
                () -> assertThat(response.getCategories().size()).isEqualTo(2L),
                () -> assertThat(response.getTags().size()).isEqualTo(2L)
        );
    }
}
