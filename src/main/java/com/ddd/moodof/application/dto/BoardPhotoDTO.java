package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.board.photo.BoardPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BoardPhotoDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class BoardPhotoResponse {
        private Long id;
        private Long storagePhotoId;
        private Long boardId;
        private Long userId;
        private Long previousBoardPhotoId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static BoardPhotoResponse from(BoardPhoto boardPhoto) {
            return new BoardPhotoResponse(boardPhoto.getId(), boardPhoto.getStoragePhotoId(), boardPhoto.getBoardId(), boardPhoto.getUserId(), boardPhoto.getPreviousBoardPhotoId(), boardPhoto.getCreatedDate(), boardPhoto.getLastModifiedDate());
        }

        public static List<BoardPhotoResponse> listFrom(List<BoardPhoto> boardPhotos) {
            return boardPhotos.stream()
                    .map(BoardPhotoResponse::from)
                    .collect(Collectors.toList());
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AddBoardPhoto {
        private List<Long> storagePhotoIds;
        private Long boardId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class RemoveBoardPhotos {
        private List<Long> boardPhotoIds;
    }
}
