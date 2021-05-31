package com.ddd.moodof.acceptance;

import com.ddd.moodof.application.dto.*;
import com.ddd.moodof.domain.model.board.Board;
import com.ddd.moodof.domain.model.board.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import static com.ddd.moodof.adapter.presentation.BoardController.API_BOARD;
import static com.ddd.moodof.adapter.presentation.PublicBoardController.API_PUBLIC_BOARDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardSharedAcceptanceTest extends AcceptanceTest{

    @Autowired
    private BoardRepository boardRepository;


    @Test
    public void 공유_URI_생성() throws Exception {
        // given
        CategoryDTO.CategoryResponse category = 카테고리_생성(userId, "title", 0L);

        // when
        BoardDTO.BoardResponse response = 보드_생성(userId, 0L, category.getId(), "name");
        Board board = boardRepository.findById(response.getId()).get();

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(board.getId()),
                () -> assertThat(response.getCategoryId()).isEqualTo(board.getCategoryId()),
                () -> assertThat(response.getSharedKey()).isEqualTo(board.getSharedKey())
        );
    }

    @Test
    public void 공유_URI_조회() throws Exception {
        // given
        CategoryDTO.CategoryResponse category = 카테고리_생성(userId, "title", 0L);
        BoardDTO.BoardResponse board = 보드_생성(userId, 0L, category.getId(), "name");

        // when
        BoardDTO.BoardSharedResponse response = getWithLogin(API_BOARD + "/" + board.getId(), BoardDTO.BoardSharedResponse.class, userId);

        // then
        assertThat(response.getSharedKey()).isEqualTo(board.getSharedKey());
    }
    
    @Test
    public void 공유_보드_조회() throws Exception {
        // given
        StoragePhotoDTO.StoragePhotoResponse storagePhoto = 보관함사진_생성(userId, "photoUri", "representativeColor");
        StoragePhotoDTO.StoragePhotoResponse storagePhoto2 = 보관함사진_생성(userId, "photoUri", "representativeColor");
        CategoryDTO.CategoryResponse category = 카테고리_생성(userId, "title", 0L);
        BoardDTO.BoardResponse board = 보드_생성(userId, 0L, category.getId(), "name");
        보드_사진_복수_생성(userId, List.of(storagePhoto.getId(), storagePhoto2.getId()), board.getId());

        // when
        List<BoardPhotoDTO.BoardPhotoResponse> response = getListNotLogin(API_PUBLIC_BOARDS , BoardPhotoDTO.BoardPhotoResponse.class,  board.getSharedKey());

        // then
        assertThat(response.size()).isEqualTo(2);
    }

    @Test
    void 공유_보드_상세조회() {
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

        TagDTO.TagCreatedResponse tag1 = 태그_생성(userId, storagePhoto1.getId(), "tag-1");
        TagDTO.TagCreatedResponse tag2 = 태그_생성(userId, storagePhoto1.getId(), "tag-2");
        태그붙이기_생성(userId, storagePhoto2.getId(), tag1.getId());
        태그붙이기_생성(userId, storagePhoto2.getId(), tag2.getId());
        보관함사진_휴지통_이동(List.of(storagePhoto3.getId(), storagePhoto4.getId()), userId);

        // when
        String uri = UriComponentsBuilder.fromUriString(API_PUBLIC_BOARDS)
                .path("/{property1}/detail/{property2}")
                .queryParam("tagIds", 0L, tag1.getId(), tag2.getId())
                .build()
                .expand(board2.getSharedKey(), storagePhoto2.getId())
                .toUriString();

        StoragePhotoDTO.StoragePhotoDetailResponse response = getNotLogin(uri, StoragePhotoDTO.StoragePhotoDetailResponse.class);

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
