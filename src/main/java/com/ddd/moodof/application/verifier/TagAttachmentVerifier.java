package com.ddd.moodof.application.verifier;

import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import com.ddd.moodof.domain.model.tag.TagRepository;
import com.ddd.moodof.domain.model.tag.attachment.TagAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TagAttachmentVerifier {
    private final StoragePhotoRepository storagePhotoRepository;
    private final TagRepository tagRepository;

    public TagAttachment toEntity(Long userId, Long storagePhotoId, Long tagId) {
        if (storagePhotoRepository.existsByIdAndUserId(storagePhotoId, userId) && tagRepository.existsByIdAndUserId(tagId, userId)) {
            return new TagAttachment(null, userId, storagePhotoId, tagId, null, null);
        }
        throw new IllegalArgumentException("요청한 보관함 사진 또는 태그가 존재하지 않습니다. storagePhotoId / tagId : " + storagePhotoId + " / " + tagId);
    }
}
