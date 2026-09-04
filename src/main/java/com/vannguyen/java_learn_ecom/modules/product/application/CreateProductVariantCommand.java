package com.vannguyen.java_learn_ecom.modules.product.application;

import java.math.BigDecimal;

public record CreateProductVariantCommand(
        Long productId,
        String sku,
        String name,
        BigDecimal price
) {
}