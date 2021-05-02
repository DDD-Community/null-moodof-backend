package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.TagAttachmentAPI;
import com.ddd.moodof.application.TagAttachmentService;
import com.ddd.moodof.application.dto.TagAttachmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping(TagAttachmentController.API_TAG_ATTACHMENT)
@RestController
public class TagAttachmentController implements TagAttachmentAPI {
    public static final String API_TAG_ATTACHMENT = "/api/tag-attachments";

    private final TagAttachmentService tagAttachmentService;

    @Override
    @PostMapping
    public ResponseEntity<TagAttachmentDTO.TagAttachmentResponse> create(@LoginUserId Long userId, @RequestBody TagAttachmentDTO.CreateTagAttachment request) {
        TagAttachmentDTO.TagAttachmentResponse response = tagAttachmentService.create(userId, request);
        return ResponseEntity.created(URI.create(API_TAG_ATTACHMENT + "/" + response.getId())).body(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginUserId Long userId, @PathVariable Long id) {
        tagAttachmentService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

}
