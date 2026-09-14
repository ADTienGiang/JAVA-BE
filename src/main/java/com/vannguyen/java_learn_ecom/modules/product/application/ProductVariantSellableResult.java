package com.vannguyen.java_learn_ecom.modules.product.application;

public record ProductVariantSellableResult(
        Long variantId,
        boolean variantActive,
        boolean stockAvailable,
        int quantity,
        boolean sellable,
        String reason
) {
}