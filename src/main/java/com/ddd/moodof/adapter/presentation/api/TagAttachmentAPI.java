package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.application.dto.TagAttachmentDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

public interface TagAttachmentAPI {
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<TagAttachmentDTO.TagAttachmentResponse> create(
            @ApiIgnore Long userId,
            @RequestBody TagAttachmentDTO.CreateTagAttachment request);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@ApiIgnore Long userId, @PathVariable Long id);
}
