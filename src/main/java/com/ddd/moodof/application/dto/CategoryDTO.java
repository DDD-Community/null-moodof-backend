package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.category.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
