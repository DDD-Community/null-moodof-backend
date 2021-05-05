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
            return new Board(null, previousBoardId, userId, name, categoryId, null, null);
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
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static BoardResponse from(Board board) {
            return new BoardResponse(board.getId(), board.getPreviousBoardId(), board.getUserId(), board.getName(), board.getCategoryId(), board.getCreatedDate(), board.getLastModifiedDate());
        }
    }
}
