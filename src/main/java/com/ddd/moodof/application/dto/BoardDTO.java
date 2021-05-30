package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class BoardDTO {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class CreateBoard {
        private Long previousBoardId;
        private Long categoryId;
        @Length(max = 20, message = "보드의 최대 글자수는 20자 입니다.")
        @NotBlank
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

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class ChangeBoardName {
        @Length(max = 20, message = "보드의 최대 글자수는 20자 입니다.")
        @NotBlank
        private String name;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class ChangeBoardSequence {
        private Long categoryId;
        private Long previousBoardId;
    }
}
