package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import com.vannguyen.java_learn_ecom.modules.product.application.InitializeProductVariantStockCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductVariantStockService;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantStock;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vannguyen.java_learn_ecom.modules.product.application.DecreaseProductVariantStockCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.RestoreProductVariantStockCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductVariantSellableResult;
@RestController
@RequestMapping("/api/products/{productId}/variants/{variantId}/stock")
public class ProductVariantStockController {

    private final ProductVariantStockService productVariantStockService;
    private final ProductVariantStockMapper productVariantStockMapper;

    public ProductVariantStockController(
            ProductVariantStockService productVariantStockService,
            ProductVariantStockMapper productVariantStockMapper
    ) {
        this.productVariantStockService = productVariantStockService;
        this.productVariantStockMapper = productVariantStockMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductVariantStockResponse>> initialize(
            @PathVariable Long productId,
            @PathVariable Long variantId,
            @Valid @RequestBody InitializeProductVariantStockRequest request
    ) {
        InitializeProductVariantStockCommand command =
                productVariantStockMapper.toCommand(productId, variantId, request);

        ProductVariantStock stock = productVariantStockService.initialize(command);
        ProductVariantStockResponse response = productVariantStockMapper.toResponse(stock);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ProductVariantStockResponse>> getByVariantId(
            @PathVariable Long productId,
            @PathVariable Long variantId
    ) {
        ProductVariantStock stock = productVariantStockService.getByVariantId(productId, variantId);
        ProductVariantStockResponse response = productVariantStockMapper.toResponse(stock);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/decrease")
    public ResponseEntity<ApiResponse<ProductVariantStockResponse>> decrease(
            @PathVariable Long productId,
            @PathVariable Long variantId,
            @Valid @RequestBody DecreaseProductVariantStockRequest request
    ) {
        DecreaseProductVariantStockCommand command =
                productVariantStockMapper.toCommand(productId, variantId, request);

        ProductVariantStock stock = productVariantStockService.decrease(command);
        ProductVariantStockResponse response = productVariantStockMapper.toResponse(stock);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/restore")
    public ResponseEntity<ApiResponse<ProductVariantStockResponse>> restore(
            @PathVariable Long productId,
            @PathVariable Long variantId,
            @Valid @RequestBody RestoreProductVariantStockRequest request
    ) {
        RestoreProductVariantStockCommand command =
                productVariantStockMapper.toCommand(productId, variantId, request);

        ProductVariantStock stock = productVariantStockService.restore(command);
        ProductVariantStockResponse response = productVariantStockMapper.toResponse(stock);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @PostMapping("/available")
    public ResponseEntity<ApiResponse<ProductVariantStockResponse>> markAvailable(
            @PathVariable Long productId,
            @PathVariable Long variantId
    ) {
        ProductVariantStock stock = productVariantStockService.markAvailable(productId, variantId);
        ProductVariantStockResponse response = productVariantStockMapper.toResponse(stock);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/unavailable")
    public ResponseEntity<ApiResponse<ProductVariantStockResponse>> markUnavailable(
            @PathVariable Long productId,
            @PathVariable Long variantId
    ) {
        ProductVariantStock stock = productVariantStockService.markUnavailable(productId, variantId);
        ProductVariantStockResponse response = productVariantStockMapper.toResponse(stock);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/sellable")
    public ResponseEntity<ApiResponse<ProductVariantSellableResponse>> getSellableState(
            @PathVariable Long productId,
            @PathVariable Long variantId
    ) {
        ProductVariantSellableResult result =
                productVariantStockService.getSellableState(productId, variantId);

        ProductVariantSellableResponse response = productVariantStockMapper.toResponse(result);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}