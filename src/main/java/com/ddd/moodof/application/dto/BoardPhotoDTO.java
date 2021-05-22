package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.board.photo.BoardPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BoardPhotoDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class BoardPhotoResponse {
        private Long id;
        private Long storagePhotoId;
        private Long boardId;
        private Long userId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static BoardPhotoResponse from(BoardPhoto boardPhoto) {
            return new BoardPhotoResponse(boardPhoto.getId(), boardPhoto.getStoragePhotoId(), boardPhoto.getBoardId(), boardPhoto.getUserId(), boardPhoto.getCreatedDate(), boardPhoto.getLastModifiedDate());
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AddBoardPhoto {
        private Long storagePhotoId;
        private Long boardId;
    }
}
