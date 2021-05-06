package com.ddd.moodof.acceptance;

import com.ddd.moodof.adapter.infrastructure.aws.S3FileUploader;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.application.dto.TagDTO;
import com.ddd.moodof.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

import static com.ddd.moodof.adapter.presentation.StoragePhotoController.API_STORAGE_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

public class StoragePhotoAcceptanceTest extends AcceptanceTest {
    private static final String BASE64 = "data:image/jpeg;base64,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";

    private Long userId;

    @MockBean
    private S3FileUploader s3FileUploader;

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
    void 사진보관함에_사진_파일을_추가한다() {
        // given
        String uploadedUri = "uploaded";
        when(s3FileUploader.upload(BASE64, userId)).thenReturn(uploadedUri);

        // when
        String requestRepresentativeColor = "representativeColor";
        StoragePhotoDTO.StoragePhotoResponse response = 보관함사진_생성(userId, BASE64, requestRepresentativeColor);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getUserId()).isEqualTo(userId),
                () -> assertThat(response.getUri()).isEqualTo(uploadedUri),
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
    void 사진보관함_페이지_태그별_조회() {
        // given
        StoragePhotoDTO.StoragePhotoResponse third = 보관함사진_생성(userId, "1", "1");
        StoragePhotoDTO.StoragePhotoResponse noContain = 보관함사진_생성(userId, "2", "2");
        StoragePhotoDTO.StoragePhotoResponse second = 보관함사진_생성(userId, "3", "3");
        StoragePhotoDTO.StoragePhotoResponse trash = 보관함사진_생성(userId, "4", "4");
        StoragePhotoDTO.StoragePhotoResponse top = 보관함사진_생성(userId, "5", "5");
        보관함사진_휴지통_이동(Collections.singletonList(trash.getId()), userId);
        TagDTO.TagResponse tag1 = 태그_생성(userId, "name1");
        TagDTO.TagResponse tag2 = 태그_생성(userId, "name2");
        TagDTO.TagResponse noSearch = 태그_생성(userId, "name3");
        태그붙이기_생성(userId, second.getId(), tag2.getId());
        태그붙이기_생성(userId, third.getId(), tag1.getId());
        태그붙이기_생성(userId, trash.getId(), tag1.getId());
        태그붙이기_생성(userId, top.getId(), tag1.getId());
        태그붙이기_생성(userId, noContain.getId(), noSearch.getId());

        // when
        String uri = UriComponentsBuilder.fromUriString(API_STORAGE_PHOTO)
                .queryParam("page", 0)
                .queryParam("size", 2)
                .queryParam("sortBy", "lastModifiedDate")
                .queryParam("descending", "true")
                .queryParam("tagIds", tag1.getId(), tag2.getId())
                .build().toUriString();

        StoragePhotoDTO.StoragePhotoPageResponse response = getWithLogin(uri, StoragePhotoDTO.StoragePhotoPageResponse.class, userId);

        // then
        assertAll(
                () -> assertThat(response.getStoragePhotos().size()).isEqualTo(2),
                () -> assertThat(response.getStoragePhotos().get(0)).usingRecursiveComparison().isEqualTo(top),
                () -> assertThat(response.getStoragePhotos().get(1)).usingRecursiveComparison().isEqualTo(second),
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
