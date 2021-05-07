package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.CategoryDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface CategoryAPI {

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<CategoryDTO.CategoryResponse> create(@RequestBody CategoryDTO.CreateCategoryRequest request, @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/{id}")
    ResponseEntity<CategoryDTO.CategoryResponse> updateName(@PathVariable Long id, @RequestBody CategoryDTO.UpdateNameCategoryRequest request, @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/{id}/{previousId}")
    ResponseEntity<CategoryDTO.CategoryResponse> updateOrder(@PathVariable Long id,@PathVariable Long previousId, @RequestBody CategoryDTO.UpdateOrderCategoryRequest request, @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping
    ResponseEntity<Void> deleteById(@LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping
    ResponseEntity<CategoryDTO.CategoryResponse> findByUserId(@LoginUserId Long userId);
}
