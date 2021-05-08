package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.category.Category;
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

        private Long targetId;

        private String title;

        private Long previousId;

        private LocalDateTime createdDate;

        private LocalDateTime lastModifiedDate;

        public static CategoryResponse from(Category category) {
            return CategoryResponse.builder()
                    .id(category.getId())
                    .title(category.getTitle())
                    .userId(category.getUserId())
                    .targetId(category.getTargetId())
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
                    .targetId(category.getTargetId())
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

        private Long targetId;

        private Long previousId;

        public Category toEntity(Long userId, String title,Long targetId, Long previousId) {
            return Category.builder()
                    .id(null)
                    .targetId(targetId)
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

        public Category toEntity(Category category, UpdateTitleCategoryRequest request) {
            return Category.builder()
                    .id(category.getId())
                    .title(request.getTitle())
                    .userId(category.getUserId())
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateOrderCategoryRequest {

        private Long targetId;

        private Long previousId;
    }
}
