package com.ddd.moodof.application.dto;

import com.ddd.moodof.domain.model.tag.attachment.TagAttachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TagAttachmentDTO {
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class CreateTagAttachment {
        private Long storagePhotoId;
        private Long tagId;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class TagAttachmentResponse {
        private Long id;
        private Long userId;
        private Long storagePhotoId;
        private Long tagId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public static TagAttachmentResponse from(TagAttachment tagAttachment) {
            return new TagAttachmentResponse(tagAttachment.getId(), tagAttachment.getUserId(), tagAttachment.getStoragePhotoId(), tagAttachment.getTagId(), tagAttachment.getCreatedDate(), tagAttachment.getLastModifiedDate());
        }
    }
}
