package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BoardDTO {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class CreateBoard {
        private Long previousBoardId;
        private Long categoryId;
        private String name;

        public Board toEntity(Long userId) {
            return setSharedKey(new Board(null, previousBoardId, userId, name, categoryId, "",null, null));
        }
        public Board setSharedKey(Board board){
            return new Board(board.getId(),board.getPreviousBoardId(),board.getUserId(), board.getName(),board.getCategoryId(), board.getSharedKey(), board.getCreatedDate(), board.getLastModifiedDate());
        }

    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class BoardResponse {
        private Long id;
        private Long previousBoardId;
        private Long userId;
        private String name;
        private Long categoryId;
        private String sharedKey;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static BoardResponse from(Board board) {
            return new BoardResponse(board.getId(), board.getPreviousBoardId(), board.getUserId(), board.getName(), board.getCategoryId(), board.getSharedKey(), board.getCreatedDate(), board.getLastModifiedDate());
        }
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class ChangeBoardName {
        private String name;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class ChangeBoardSequence {
        private Long categoryId;
        private Long previousBoardId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class BoardSharedRequest{
        private Long id;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class BoardSharedResponse {
        private Long id;

        private String sharedURI;

        private String sharedKey;

        public static BoardDTO.BoardSharedResponse from(Long id, String sharedURL, String sharedKey) {
            return new BoardDTO.BoardSharedResponse(id, sharedURL, sharedKey);
        }
    }


}
