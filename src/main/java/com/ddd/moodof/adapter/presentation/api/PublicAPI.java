package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.PublicController;
import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PublicAPI {
    @GetMapping(PublicController.BOARDS)
    ResponseEntity<List<CategoryDTO.CategoryResponse>> getSharedBoard(@PathVariable String sharedKey);
}
