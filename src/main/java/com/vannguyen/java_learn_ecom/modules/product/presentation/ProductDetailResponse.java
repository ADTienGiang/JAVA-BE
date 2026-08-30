package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;

import java.math.BigDecimal;

public record ProductDetailResponse(
        Long id,
        String name,
        BigDecimal price,
        ProductStatus status
) {
}