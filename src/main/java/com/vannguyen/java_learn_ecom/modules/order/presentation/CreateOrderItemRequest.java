package com.vannguyen.java_learn_ecom.modules.order.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull(message = "Product id must not be null")
        Long productId,

        @NotNull(message = "Variant id must not be null")
        Long variantId,

        @Min(value = 1, message = "Quantity must be greater than zero")
        int quantity
) {
}