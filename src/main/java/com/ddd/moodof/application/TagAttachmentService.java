package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.TagAttachmentDTO;
import com.ddd.moodof.domain.model.tag.attachment.TagAttachment;
import com.ddd.moodof.domain.model.tag.attachment.TagAttachmentRepository;
import com.ddd.moodof.domain.model.tag.attachment.TagAttachmentVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TagAttachmentService {
    private final TagAttachmentRepository tagAttachmentRepository;
    private final TagAttachmentVerifier tagAttachmentVerifier;

    public TagAttachmentDTO.TagAttachmentResponse create(Long userId, TagAttachmentDTO.CreateTagAttachment request) {
        TagAttachment tagAttachment = tagAttachmentVerifier.toEntity(userId, request.getStoragePhotoId(), request.getTagId());
        TagAttachment saved = tagAttachmentRepository.save(tagAttachment);
        return TagAttachmentDTO.TagAttachmentResponse.from(saved);
    }

    public void delete(Long id, Long userId) {
        if (!tagAttachmentRepository.existsByIdAndUserId(id, userId)) {
            throw new IllegalArgumentException("요청과 일치하는 TagAttachment가 없습니다. id / userId: " + id + " / " + userId);
        }
        tagAttachmentRepository.deleteById(id);
    }
}
