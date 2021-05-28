package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.TagAttachmentDTO;
import com.ddd.moodof.application.dto.TagDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import com.ddd.moodof.domain.model.tag.Tag;
import com.ddd.moodof.domain.model.tag.TagRepository;
import com.ddd.moodof.domain.model.tag.attachment.TagAttachment;
import com.ddd.moodof.domain.model.tag.attachment.TagAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TagCreator {
    private final TagRepository tagRepository;
    private final TagAttachmentRepository tagAttachmentRepository;
    private final StoragePhotoRepository storagePhotoRepository;

    @Transactional
    public TagDTO.TagCreatedResponse create(String name, Long userId, Long storagePhotoId) {
        if (!storagePhotoRepository.existsByIdAndUserId(storagePhotoId, userId)) {
            throw new IllegalArgumentException("태그 생성자의 아이디 및 StoragePhotoId와 일치하는 StoragePhoto가 존재하지 않습니다.");
        }
        Tag tag = tagRepository.save(new Tag(null, userId, name, null, null));
        TagAttachment tagAttachment = tagAttachmentRepository.save(new TagAttachment(null, userId, storagePhotoId, tag.getId(), null, null));
        return new TagDTO.TagCreatedResponse(tag.getId(), tag.getUserId(), tag.getName(), tag.getCreatedDate(), tag.getLastModifiedDate(), TagAttachmentDTO.TagAttachmentResponse.from(tagAttachment));
    }
}
