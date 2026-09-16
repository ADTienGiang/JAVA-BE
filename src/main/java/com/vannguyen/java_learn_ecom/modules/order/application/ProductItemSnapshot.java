package com.vannguyen.java_learn_ecom.modules.order.application;

import java.math.BigDecimal;

public record ProductItemSnapshot(
        Long productId,
        Long variantId,
        String productName,
        String variantName,
        BigDecimal unitPrice
) {
}