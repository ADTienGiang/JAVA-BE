package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.modules.product.application.ProductPreviewCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductPreviewResult;
import org.springframework.stereotype.Component;

@Component
public class ProductPreviewMapper {

    public ProductPreviewCommand toCommand(CreateProductRequest request) {
        return new ProductPreviewCommand(
                request.name(),
                request.price()
        );
    }

    public ProductPreviewResponse toResponse(ProductPreviewResult result) {
        return new ProductPreviewResponse(
                result.name(),
                result.price(),
                result.status()
        );
    }
}