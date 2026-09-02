package com.vannguyen.java_learn_ecom.modules.product.application;

import java.math.BigDecimal;

public record CreateProductCommand(
        Long categoryId,
        String name,
        String description,
        BigDecimal price
) {
}