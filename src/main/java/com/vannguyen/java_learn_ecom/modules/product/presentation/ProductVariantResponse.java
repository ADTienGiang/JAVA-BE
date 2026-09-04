package com.vannguyen.java_learn_ecom.modules.product.presentation;

import java.math.BigDecimal;

public record ProductVariantResponse(
        Long id,
        Long productId,
        String sku,
        String name,
        BigDecimal price,
        boolean active
) {
}