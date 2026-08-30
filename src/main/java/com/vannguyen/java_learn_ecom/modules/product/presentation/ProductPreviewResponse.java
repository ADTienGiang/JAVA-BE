package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;

import java.math.BigDecimal;

public record ProductPreviewResponse(
        String name,
        BigDecimal price,
        ProductStatus status
) {
}