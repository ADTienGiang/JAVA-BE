package com.vannguyen.java_learn_ecom.modules.product.application;

public record InitializeProductVariantStockCommand(
        Long productId,
        Long variantId,
        int quantity
) {
}