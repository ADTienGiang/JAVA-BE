package com.vannguyen.java_learn_ecom.modules.product.presentation;

public record ProductVariantStockResponse(
        Long id,
        Long variantId,
        int quantity,
        boolean available
) {
}