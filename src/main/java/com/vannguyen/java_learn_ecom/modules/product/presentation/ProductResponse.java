package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        Long categoryId,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status
) {
}