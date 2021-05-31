package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.category.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
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

        public static List<CategoryResponse> listFrom(List<Category> categories) {
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
    public static class CategoryWithBoardResponse {

        private Long id;

        private Long userId;

        private String title;

        private Long previousId;

        private LocalDateTime createdDate;

        private LocalDateTime lastModifiedDate;

        private List<BoardDTO.BoardResponse> boardList;

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
        @Length(max = 20, message = "카테고리의 최대 글자수는 20자 입니다.")
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

        public Category toEntity(Long userId, String title, Long previousId) {
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
        @Length(max = 20, message = "카테고리의 최대 글자수는 20자 입니다.")
        @NotBlank
        private String title;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateOrderCategoryRequest {
        private Long previousId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CategoryDetailResponse {
        private Long id;
        private Long userId;
        private String title;
        private Long previousId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
        private List<BoardDTO.BoardResponse> boards;

        @QueryProjection
        public CategoryDetailResponse(Long id, Long userId, String title, Long previousId, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
            this.id = id;
            this.userId = userId;
            this.title = title;
            this.previousId = previousId;
            this.createdDate = createdDate;
            this.lastModifiedDate = lastModifiedDate;
        }

        public void setBoards(List<BoardDTO.BoardResponse> boards) {
            this.boards = boards;
        }
    }
}
