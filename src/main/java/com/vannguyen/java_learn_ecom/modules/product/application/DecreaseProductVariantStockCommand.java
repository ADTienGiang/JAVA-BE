package com.vannguyen.java_learn_ecom.modules.product.application;

public record DecreaseProductVariantStockCommand(
        Long productId,
        Long variantId,
        int quantity
) {
}