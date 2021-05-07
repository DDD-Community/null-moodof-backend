package com.ddd.moodof.adapter.presentation;
import com.ddd.moodof.adapter.presentation.api.CategoryAPI;
import com.ddd.moodof.application.CategoryService;
import com.ddd.moodof.application.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping(CategoryController.API_CATEGORY)
@RestController
public class CategoryController implements CategoryAPI {
    public static final String API_CATEGORY = "/api/category";
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
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO.CategoryResponse> updateName(@PathVariable Long id, CategoryDTO.UpdateNameCategoryRequest request, Long userId) {
        return null;
    }

    @Override
    @PutMapping("/{id}/{previousId}")
    public ResponseEntity<CategoryDTO.CategoryResponse> updateOrder(@PathVariable Long id, @PathVariable Long previousId,CategoryDTO.UpdateOrderCategoryRequest request, Long userId) {
        return null;
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteById(@LoginUserId Long userId) {
        return null;
    }

    @Override
    @GetMapping
    public ResponseEntity<CategoryDTO.CategoryResponse> findByUserId(@LoginUserId Long userId) {
        return null;
    }
}
