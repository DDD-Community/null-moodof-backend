package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.TagDTO;
import com.ddd.moodof.domain.model.tag.Tag;
import com.ddd.moodof.domain.model.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    public List<TagDTO.TagResponse> findAllByTag(Long userId) {
        List<Tag> allByUserId = tagRepository.findAllByUserId(userId);
        return TagDTO.TagResponse.listFrom(allByUserId);
    }

    public void delete(Long id, Long userId) {
        Tag tag = tagRepository.findTagByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그 정보입니다. " + id + " / " + userId));
        tagRepository.deleteById(tag.getId());
    }

    public TagDTO.TagResponse update(Long id, TagDTO.UpdateRequest request, Long userId) {
        Tag tagExist = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그 입니다."));
        if (!tagExist.getUserId().equals(userId)) throw new IllegalArgumentException("올바른 유저 정보가 아닙니다.");
        return TagDTO.TagResponse.from(
                tagRepository.save(request.toEntity(tagExist.getId(), tagExist.getUserId(), request.getName()))
        );
    }
}
