package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import com.vannguyen.java_learn_ecom.modules.product.application.CreateProductCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductService;
import com.vannguyen.java_learn_ecom.modules.product.domain.Category;
import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.lang.reflect.Parameter;
import com.vannguyen.java_learn_ecom.modules.product.application.UpdateProductCommand;

import com.vannguyen.java_learn_ecom.modules.product.application.ProductSearchQuery;
import java.util.List;
import com.vannguyen.java_learn_ecom.common.dto.PageResult;
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(
            ProductService productService,
            ProductMapper productMapper
    ) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @Valid @RequestBody CreateProductRequest request
    ) {
        CreateProductCommand command = productMapper.toCommand(request);
        Product product = productService.create(command);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResult<ProductResponse>>> search(
            @Valid ProductSearchRequest request
    ) {
        ProductSearchQuery query = productMapper.toQuery(request);

        PageResult<Product> productPage = productService.search(query);

        List<ProductResponse> items = productPage.items()
                .stream()
                .map(productMapper::toResponse)
                .toList();

        PageResult<ProductResponse> response = new PageResult<>(
                items,
                productPage.page(),
                productPage.size(),
                productPage.totalItems(),
                productPage.totalPages(),
                productPage.hasNext(),
                productPage.hasPrevious()
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> findById(
            @PathVariable Long id
    ) {
        Product product = productService.findById(id);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAll() {
        List<ProductResponse> response = productService.findAllActive()
                .stream()
                .map(productMapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        UpdateProductCommand command = productMapper.toCommand(id, request);
        Product product = productService.update(command);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> deactivate(
            @PathVariable Long id
    ) {
        Product product = productService.deactivate(id);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


}