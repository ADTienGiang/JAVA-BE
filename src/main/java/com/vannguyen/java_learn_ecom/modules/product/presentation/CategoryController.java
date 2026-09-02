package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import com.vannguyen.java_learn_ecom.modules.product.application.CategoryService;
import com.vannguyen.java_learn_ecom.modules.product.application.CreateCategoryCommand;
import com.vannguyen.java_learn_ecom.modules.product.domain.Category;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.vannguyen.java_learn_ecom.modules.product.application.UpdateCategoryCommand;
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(
            CategoryService categoryService,
            CategoryMapper categoryMapper
    ) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        CreateCategoryCommand command = categoryMapper.toCommand(request);
        Category category = categoryService.create(command);
        CategoryResponse response = categoryMapper.toResponse(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> findById(
            @PathVariable Long id
    ) {
        Category category = categoryService.findById(id);
        CategoryResponse response = categoryMapper.toResponse(category);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> findAll() {
        List<CategoryResponse> response = categoryService.findAllActive()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        UpdateCategoryCommand command = categoryMapper.toCommand(id, request);
        Category category = categoryService.update(command);
        CategoryResponse response = categoryMapper.toResponse(category);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> deactivate(
            @PathVariable Long id
    ) {
        Category category = categoryService.deactivate(id);
        CategoryResponse response = categoryMapper.toResponse(category);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}