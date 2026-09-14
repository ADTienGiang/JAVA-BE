package com.vannguyen.java_learn_ecom.modules.product.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InitializeProductVariantStockRequest(

        @NotNull(message = "Stock quantity must not be null")
        @Min(value = 0, message = "Stock quantity must not be negative")
        Integer quantity
) {
}