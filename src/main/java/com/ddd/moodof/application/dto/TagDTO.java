package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TagDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CreateRequest {
        private Long storagePhotoId;
        @NotBlank
        private String name;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateRequest {
        @NotBlank
        private String name;

        public Tag toEntity(Long id, Long userId, String name) {
            return Tag.builder()
                    .id(id)
                    .userId(userId)
                    .name(name)
                    .createdDate(null)
                    .lastModifiedDate(null)
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class TagResponse {
        private Long id;
        private Long userId;
        private String name;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static TagResponse from(Tag tag) {
            return TagResponse.builder()
                    .id(tag.getId())
                    .userId(tag.getUserId())
                    .name(tag.getName())
                    .createdDate(tag.getCreatedDate())
                    .lastModifiedDate(tag.getLastModifiedDate())
                    .build();
        }

        public static List<TagResponse> listFrom(List<Tag> tagList) {
            return tagList.stream().map(tag -> TagResponse.builder()
                    .id(tag.getId())
                    .userId(tag.getUserId())
                    .name(tag.getName())
                    .createdDate(tag.getCreatedDate())
                    .lastModifiedDate(tag.getLastModifiedDate())
                    .build()).collect(Collectors.toList());
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TagCreatedResponse {
        private Long id;
        private Long userId;
        private String name;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
        private TagAttachmentDTO.TagAttachmentResponse tagAttachment;
    }
}
