package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.TagAPI;
import com.ddd.moodof.application.TagService;
import com.ddd.moodof.application.dto.TagDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(TagController.API_TAG)
@RestController
public class TagController implements TagAPI {
    public static final String API_TAG = "/api/tags";
    private final TagService tagService;

    @Override
    @GetMapping
    public ResponseEntity<List<TagDTO.TagResponse>> findAllByTag(@LoginUserId Long userId) {
        List<TagDTO.TagResponse> allByTag = tagService.findAllByTag(userId);
        return ResponseEntity.ok(allByTag);
    }

    @Override
    @PostMapping
    public ResponseEntity<TagDTO.TagResponse> create(
            @RequestBody @Valid TagDTO.CreateRequest request,
            @LoginUserId Long userId) {
        TagDTO.TagResponse response = tagService.create(request, userId);
        return ResponseEntity.created(URI.create(API_TAG + "/" + response.getId())).body(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginUserId Long userId, @PathVariable Long id) {
        tagService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TagDTO.TagResponse> update(@PathVariable Long id, @RequestBody @Valid TagDTO.UpdateRequest request, @LoginUserId Long userId) {
        TagDTO.TagResponse response = tagService.update(id, request, userId);
        return ResponseEntity.ok(response);
    }
}
