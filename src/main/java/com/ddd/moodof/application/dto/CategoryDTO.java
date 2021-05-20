package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.board.Board;
import com.ddd.moodof.domain.model.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CategoryResponse {

        private Long id;

        private Long userId;

        private String title;

        private Long previousId;

        private LocalDateTime createdDate;

        private LocalDateTime lastModifiedDate;

        public static CategoryResponse from(Category category) {
            return CategoryResponse.builder()
                    .id(category.getId())
                    .title(category.getTitle())
                    .userId(category.getUserId())
                    .previousId(category.getPreviousId())
                    .createdDate(category.getCreatedDate())
                    .lastModifiedDate(category.getLastModifiedDate())
                    .build();
        }

        public static List<CategoryResponse> listForm(List<Category> categories) {
            return categories.stream().map(category -> CategoryResponse.builder()
                    .id(category.getId())
                    .userId(category.getUserId())
                    .title(category.getTitle())
                    .previousId(category.getPreviousId())
                    .createdDate(category.getCreatedDate())
                    .lastModifiedDate(category.getLastModifiedDate())
                    .build()).collect(Collectors.toList());
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CategoryWithBoardResponse{

        private Long id;

        private Long userId;

        private String title;

        private Long previousId;

        private LocalDateTime createdDate;

        private LocalDateTime lastModifiedDate;

        private List<BoardDTO.BoardResponse> boardList = new ArrayList<>();

        public static CategoryWithBoardResponse from(CategoryDTO.CategoryResponse category, List<BoardDTO.BoardResponse> boards) {
            return CategoryWithBoardResponse.builder()
                    .id(category.getId())
                    .title(category.getTitle())
                    .userId(category.getUserId())
                    .previousId(category.getPreviousId())
                    .createdDate(category.getCreatedDate())
                    .lastModifiedDate(category.getLastModifiedDate())
                    .boardList(boards)
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CreateCategoryRequest {
        @NotBlank
        private String title;

        private Long previousId;

        public Category toEntity(Long userId) {
            return Category.builder()
                    .id(null)
                    .previousId(previousId)
                    .title(title)
                    .userId(userId)
                    .createdDate(null)
                    .lastModifiedDate(null)
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateTitleCategoryRequest {
        @NotBlank
        private String title;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateOrderCategoryRequest {
        private Long previousId;
    }
}
