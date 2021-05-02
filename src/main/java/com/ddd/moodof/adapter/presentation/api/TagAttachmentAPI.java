package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.application.dto.TagAttachmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

public interface TagAttachmentAPI {
    @PostMapping
    ResponseEntity<TagAttachmentDTO.TagAttachmentResponse> create(
            @ApiIgnore Long userId,
            @RequestBody TagAttachmentDTO.CreateTagAttachment request);
}
