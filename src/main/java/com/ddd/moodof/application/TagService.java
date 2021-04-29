package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.TagDTO;
import com.ddd.moodof.domain.model.tag.Tag;
import com.ddd.moodof.domain.model.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;
    public List<TagDTO.TagResponse> findAllByTag(Long userId) {
        List<Tag> allByUserId = tagRepository.findAllByUserId(userId);
        return TagDTO.TagResponse.listFrom(allByUserId);
    }

    public TagDTO.TagResponse create(TagDTO.CreateRequest request, Long userId) {
        Optional<Tag> tagExist = tagRepository.findTagByNameAndUserId(request.getName(), userId);
        if(tagExist.isPresent()) {
            throw new IllegalArgumentException("존재하는 태그 입니다.");
        }
        Tag save = tagRepository.save(request.toEntity(userId, request.getName()));
        return TagDTO.TagResponse.from(save);
    }

    public void delete(Long id, Long userId) {
        Tag tag = tagRepository.findTagByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그 정보입니다. " + id + " / " + userId));
        if (userId.compareTo(tag.getUserId()) != 0) {
            throw new IllegalArgumentException("요청 응답 유저의 값이 다른 정보입니다.");
        }
        tagRepository.deleteById(tag.getId());
    }

    public TagDTO.TagResponse update(Long id, TagDTO.UpdateRequest request, Long userId) {
        Tag tagExist = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그 입니다."));
        if(!tagExist.getUserId().equals(userId)) throw new IllegalArgumentException("올바른 유저 정보가 아닙니다.");
        return TagDTO.TagResponse.from(
                tagRepository.save(request.toEntity(tagExist.getId(),tagExist.getUserId(),request.getName()))
        );
    }
}
