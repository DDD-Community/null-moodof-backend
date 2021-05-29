package com.ddd.moodof.acceptance;

import com.ddd.moodof.adapter.infrastructure.configuration.EncryptConfig;
import com.ddd.moodof.adapter.infrastructure.security.encrypt.EncryptUtil;
import com.ddd.moodof.application.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import static com.ddd.moodof.adapter.presentation.PublicController.API_PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardSharedAcceptanceTest extends AcceptanceTest{

    @Autowired
    private EncryptConfig encryptConfig;

    @Test
    public void 공유_URL_생성() throws Exception {
        // given
        CategoryDTO.CategoryResponse category = 카테고리_생성(userId, "title", 0L);
        BoardDTO.BoardResponse board = 보드_생성(userId, 0L, category.getId(), "name");

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
    public void 공유_보드_조회() throws Exception {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto2 = 보관함사진_생성(userId, "photoUri", "representativeColor");
        CategoryDTO.CategoryResponse category = 카테고리_생성(userId, "title", 0L);
        BoardDTO.BoardResponse board = 보드_생성(userId, 0L, category.getId(), "name");

        // when
        보드_사진_복수_생성(userId, List.of(storagePhoto.getId(), storagePhoto2.getId()), board.getId());
        BoardDTO.BoardSharedResponse response = 보드_공유하기_생성(board.getId(), userId);
        List<BoardPhotoDTO.BoardPhotoResponse> responses = getListNotLogin(API_PUBLIC + "/boards" , BoardPhotoDTO.BoardPhotoResponse.class,  response.getSharedKey());
        // then
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void 공유_보관함사진_상세_조회() {
        // given
        CategoryDTO.CategoryResponse category = 카테고리_생성(userId, "title", 0L);
        CategoryDTO.CategoryResponse category2 = 카테고리_생성(userId, "title", category.getId());

        StoragePhotoDTO.StoragePhotoResponse storagePhoto1 = 보관함사진_생성(userId, "1", "1");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto2 = 보관함사진_생성(userId, "2", "2");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto3 = 보관함사진_생성(userId, "3", "3");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto4 = 보관함사진_생성(userId, "4", "4");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto5 = 보관함사진_생성(userId, "5", "5");

        BoardDTO.BoardResponse board1 = 보드_생성(userId, 0L, category.getId(), "board-1");
        BoardDTO.BoardResponse board2 = 보드_생성(userId, board1.getId(), category.getId(), "board-2");
        BoardDTO.BoardResponse board3 = 보드_생성(userId, 0L, category2.getId(), "board-3");
        보드_사진_복수_생성(userId, List.of(storagePhoto1.getId(), storagePhoto2.getId()), board1.getId());
        보드_사진_복수_생성(userId, List.of(storagePhoto2.getId()), board2.getId());
        보드_사진_복수_생성(userId, List.of(storagePhoto2.getId()), board3.getId());
        보드_사진_복수_생성(userId, List.of(storagePhoto3.getId()), board1.getId());
        보드_사진_복수_생성(userId, List.of(storagePhoto4.getId()), board2.getId());
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
