package com.ddd.moodof.application;

import com.ddd.moodof.adapter.infrastructure.advice.ValidationExceptionResponse;
import com.ddd.moodof.application.dto.TagDTO;
import com.ddd.moodof.domain.model.tag.Tag;
import com.ddd.moodof.domain.model.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.ValidationException;
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

    public TagDTO.TagResponse create(TagDTO.CreateRequest request, Long userId) throws ValidationExceptionResponse {
        Optional<Tag> tagExist = tagRepository.findTagByTagNameAndUserId(request.getTagName(), userId);
        if(tagExist.isPresent()) {
            throw new ValidationExceptionResponse(HttpStatus.CONFLICT, "존재하는 태그 입니다.");
        }
        Tag save = tagRepository.save(request.toEntity(userId, request.getTagName()));
        return TagDTO.TagResponse.from(save);
    }

    public Long delete(Long id, Long userId) {
        Optional<Tag> response = Optional.ofNullable(tagRepository.findTagByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그 정보입니다. " + id + " / " + userId)));
        return tagRepository.deleteByIdAndUserId(response.get().getId(),userId);
    }

    public TagDTO.TagResponse update(TagDTO.@Valid UpdateReqeust reqeust, Long userId) throws ValidationExceptionResponse {
        Optional<Tag> tagExist = tagRepository.findTagByTagNameAndUserId(reqeust.getTagName(), userId);
        if(!tagExist.isPresent()) {
            throw new ValidationExceptionResponse(HttpStatus.CONFLICT, "존재하지 않는 태그 입니다.");
        }
        Tag reponse = tagRepository.save(reqeust.toEntity(userId, reqeust.getTagName()));
        return TagDTO.TagResponse.from(reponse);
    }
}
