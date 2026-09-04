package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import com.vannguyen.java_learn_ecom.modules.product.application.CreateProductVariantCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductVariantService;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariant;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.vannguyen.java_learn_ecom.modules.product.application.UpdateProductVariantCommand;

@RestController
@RequestMapping("/api/products/{productId}/variants")
public class ProductVariantController {

    private final ProductVariantService productVariantService;
    private final ProductVariantMapper productVariantMapper;

    public ProductVariantController(
            ProductVariantService productVariantService,
            ProductVariantMapper productVariantMapper
    ) {
        this.productVariantService = productVariantService;
        this.productVariantMapper = productVariantMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductVariantResponse>> create(
            @PathVariable Long productId,
            @Valid @RequestBody CreateProductVariantRequest request
    ) {
        CreateProductVariantCommand command = productVariantMapper.toCommand(productId, request);
        ProductVariant variant = productVariantService.create(command);
        ProductVariantResponse response = productVariantMapper.toResponse(variant);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> findAllActiveByProductId(
            @PathVariable Long productId
    ) {
        List<ProductVariantResponse> response = productVariantService.findAllActiveByProductId(productId)
                .stream()
                .map(productVariantMapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> update(
            @PathVariable Long productId,
            @PathVariable Long variantId,
            @Valid @RequestBody UpdateProductVariantRequest request
    ) {
        UpdateProductVariantCommand command = productVariantMapper.toCommand(productId, variantId, request);
        ProductVariant variant = productVariantService.update(command);
        ProductVariantResponse response = productVariantMapper.toResponse(variant);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> deactivate(
            @PathVariable Long productId,
            @PathVariable Long variantId
    ) {
        ProductVariant variant = productVariantService.deactivate(productId, variantId);
        ProductVariantResponse response = productVariantMapper.toResponse(variant);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
    


}