package com.vannguyen.java_learn_ecom.modules.order.application;

public record CreateOrderItemCommand(
        Long productId,
        Long variantId,
        int quantity
) {
}