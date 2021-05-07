package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CategoryDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CategoryResponse {
        private Long id;
        private Long userId;
        private String name;
        private Long previousId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static CategoryResponse from(Category category) {
            return CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .userId(category.getUserId())
                    .previousId(category.getPreviousId())
                    .createdDate(category.getCreatedDate())
                    .lastModifiedDate(category.getLastModifiedDate())
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CreateCategoryRequest {
        @NotBlank
        private String name;

        public Category toEntity(Long userId, String name) {
            return Category.builder()
                    .id(null)
                    .previousId(null)
                    .name(name)
                    .userId(userId)
                    .createdDate(null)
                    .lastModifiedDate(null)
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateNameCategoryRequest {
        @NotBlank
        private String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateOrderCategoryRequest {
        private Long id;
        private Long previousId;
    }
}
