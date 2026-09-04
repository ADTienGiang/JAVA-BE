package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.modules.product.application.CreateProductVariantCommand;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariant;
import org.springframework.stereotype.Component;
import com.vannguyen.java_learn_ecom.modules.product.application.UpdateProductVariantCommand;
@Component
public class ProductVariantMapper {

    public CreateProductVariantCommand toCommand(
            Long productId,
            CreateProductVariantRequest request
    ) {
        return new CreateProductVariantCommand(
                productId,
                request.sku(),
                request.name(),
                request.price()
        );
    }

    public UpdateProductVariantCommand toCommand(
            Long productId,
            Long variantId,
            UpdateProductVariantRequest request
    ) {
        return new UpdateProductVariantCommand(
                productId,
                variantId,
                request.sku(),
                request.name(),
                request.price()
        );
    }
    
    public ProductVariantResponse toResponse(ProductVariant variant) {
        return new ProductVariantResponse(
                variant.getId(),
                variant.getProductId(),
                variant.getSku(),
                variant.getName(),
                variant.getPrice(),
                variant.isActive()
        );
    }



}