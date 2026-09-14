package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.modules.product.application.InitializeProductVariantStockCommand;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantStock;
import org.springframework.stereotype.Component;
import com.vannguyen.java_learn_ecom.modules.product.application.DecreaseProductVariantStockCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.RestoreProductVariantStockCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductVariantSellableResult;
@Component
public class ProductVariantStockMapper {

    public InitializeProductVariantStockCommand toCommand(
            Long productId,
            Long variantId,
            InitializeProductVariantStockRequest request
    ) {
        return new InitializeProductVariantStockCommand(
                productId,
                variantId,
                request.quantity()
        );
    }

    public ProductVariantStockResponse toResponse(ProductVariantStock stock) {
        return new ProductVariantStockResponse(
                stock.getId(),
                stock.getVariantId(),
                stock.getQuantity(),
                stock.isAvailable()
        );
    }
    public DecreaseProductVariantStockCommand toCommand(
            Long productId,
            Long variantId,
            DecreaseProductVariantStockRequest request
    ) {
        return new DecreaseProductVariantStockCommand(
                productId,
                variantId,
                request.quantity()
        );
    }

    public RestoreProductVariantStockCommand toCommand(
            Long productId,
            Long variantId,
            RestoreProductVariantStockRequest request
    ) {
        return new RestoreProductVariantStockCommand(
                productId,
                variantId,
                request.quantity()
        );
    }


    public ProductVariantSellableResponse toResponse(ProductVariantSellableResult result) {
        return new ProductVariantSellableResponse(
                result.variantId(),
                result.variantActive(),
                result.stockAvailable(),
                result.quantity(),
                result.sellable(),
                result.reason()
        );
    }
}