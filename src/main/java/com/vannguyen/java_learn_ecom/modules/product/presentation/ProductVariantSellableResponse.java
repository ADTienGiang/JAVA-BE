package com.vannguyen.java_learn_ecom.modules.product.presentation;

public record ProductVariantSellableResponse(
        Long variantId,
        boolean variantActive,
        boolean stockAvailable,
        int quantity,
        boolean sellable,
        String reason
) {
}