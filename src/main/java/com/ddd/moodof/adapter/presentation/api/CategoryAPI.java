package com.ddd.moodof.adapter.presentation.api;

import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.CategoryDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CategoryAPI {

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    ResponseEntity<CategoryDTO.CategoryResponse> create(@RequestBody CategoryDTO.CreateCategoryRequest request, @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/{id}")
    ResponseEntity<CategoryDTO.CategoryResponse> updateTitle(@PathVariable Long id, @RequestBody CategoryDTO.UpdateTitleCategoryRequest request, @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/order/{id}")
    ResponseEntity<CategoryDTO.CategoryResponse> updateOrder(@PathVariable Long id, @RequestBody CategoryDTO.UpdateOrderCategoryRequest request, @LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable Long id,@LoginUserId Long userId);

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping
    ResponseEntity<List<CategoryDTO.CategoryResponse>> findAllByUserId(@LoginUserId Long userId);
}
