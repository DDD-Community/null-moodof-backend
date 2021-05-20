package com.ddd.moodof.adapter.presentation;
import com.ddd.moodof.adapter.presentation.api.CategoryAPI;
import com.ddd.moodof.application.CategoryService;
import com.ddd.moodof.application.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(CategoryController.API_CATEGORY)
@RestController
public class CategoryController implements CategoryAPI {
    public static final String API_CATEGORY = "/api/categories";
    private final CategoryService categoryService;

    @Override
    @PostMapping
    public ResponseEntity<CategoryDTO.CategoryResponse> create(
            @RequestBody CategoryDTO.CreateCategoryRequest request,
            @LoginUserId Long userId) {
        CategoryDTO.CategoryResponse response = categoryService.create(request, userId);
        return ResponseEntity.created(URI.create(API_CATEGORY + "/" + response.getId())).body(response);
    }

    @Override
    @PutMapping("/{id}/title")
    public ResponseEntity<CategoryDTO.CategoryResponse> updateTitle(
            @PathVariable Long id,
            @RequestBody CategoryDTO.UpdateTitleCategoryRequest request,
            @LoginUserId Long userId) {
        CategoryDTO.CategoryResponse response = categoryService.updateTitle(request, id, userId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/{id}/previousId")
    public ResponseEntity<CategoryDTO.CategoryResponse> updatePreviousId(
            @PathVariable Long id,
            @RequestBody CategoryDTO.UpdateOrderCategoryRequest request,
            @LoginUserId Long userId) {
        CategoryDTO.CategoryResponse response = categoryService.updatePreviousId(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id,
            @LoginUserId Long userId) {
        categoryService.deleteById(id,userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CategoryDTO.CategoryResponse>>findCategoryByUserId(@LoginUserId Long userId) {
        return ResponseEntity.ok(categoryService.findCategoryByUserId(userId));
    }

    @Override
    @GetMapping("/boards")
    public ResponseEntity<List<CategoryDTO.CategoryWithBoardResponse>> findCategoryWithBoardResponse(
            @LoginUserId Long userId) {
        return ResponseEntity.ok(categoryService.findCategoryWithBoardByUserId(userId));
    }
}
