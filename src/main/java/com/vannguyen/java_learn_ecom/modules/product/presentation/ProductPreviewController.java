package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductPreviewCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductPreviewResult;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductPreviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductPreviewController {

    private final ProductPreviewService productPreviewService;
    private final ProductPreviewMapper productPreviewMapper;

    public ProductPreviewController(
            ProductPreviewService productPreviewService,
            ProductPreviewMapper productPreviewMapper
    ) {
        this.productPreviewService = productPreviewService;
        this.productPreviewMapper = productPreviewMapper;
    }

    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<ProductPreviewResponse>> preview(
            @Valid @RequestBody CreateProductRequest request
    ) {
        ProductPreviewCommand command = productPreviewMapper.toCommand(request);
        ProductPreviewResult result = productPreviewService.preview(command);
        ProductPreviewResponse response = productPreviewMapper.toResponse(result);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getPreviewById(
            @PathVariable Long id
    ) {
        ProductDetailResponse response = productPreviewService.getPreviewById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/preview/search")
    public ResponseEntity<ApiResponse<List<ProductPreviewResponse>>> searchPreview(
            @RequestParam String keyword
    ) {
        List<ProductPreviewResponse> response = productPreviewService.searchPreview(keyword);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}