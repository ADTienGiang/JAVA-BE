package com.vannguyen.java_learn_ecom.modules.product.infrastructure.cache;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;

import java.math.BigDecimal;

public record ProductCacheValue(
        Long id,
        Long version,
        Long categoryId,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status
) {
}