package com.vannguyen.java_learn_ecom.modules.product.application;

import java.math.BigDecimal;

public record UpdateProductVariantCommand(
        Long productId,
        Long variantId,
        String sku,
        String name,
        BigDecimal price
) {
}