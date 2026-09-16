package com.vannguyen.java_learn_ecom.modules.order.presentation;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        Long variantId,
        String productName,
        String variantName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal
) {
}