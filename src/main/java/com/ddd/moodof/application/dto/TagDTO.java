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
    public static class CreateRequest{
        @NotBlank
        private String tagName;

        public Tag toEntity(Long userId, String tagName) {
            return Tag.builder()
                    .id(null)
                    .userId(userId)
                    .tagName(tagName)
                    .createdTime(null)
                    .lastModifiedDate(null)
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateReqeust{
        @NotBlank
        private String tagName;

        public Tag toEntity(Long userId, String tagName) {
            return Tag.builder()
                    .id(null)
                    .userId(userId)
                    .tagName(tagName)
                    .createdTime(null)
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
        private String tagName;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static TagResponse from(Tag tag){
            return TagResponse.builder()
                    .id(tag.getId())
                    .userId(tag.getUserId())
                    .tagName(tag.getTagName())
                    .createdDate(tag.getCreatedTime())
                    .lastModifiedDate(tag.getLastModifiedDate())
                    .build();
        }
        public static List<TagResponse> listFrom(List<Tag> tagList){
            return tagList.stream().map(tag -> TagResponse.builder()
                    .id(tag.getId())
                    .userId(tag.getUserId())
                    .tagName(tag.getTagName())
                    .createdDate(tag.getCreatedTime())
                    .lastModifiedDate(tag.getLastModifiedDate())
                    .build()).collect(Collectors.toList());
        }
    }
}
