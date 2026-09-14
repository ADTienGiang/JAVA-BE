package com.vannguyen.java_learn_ecom.modules.product.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DecreaseProductVariantStockRequest(

        @NotNull(message = "Stock quantity must not be null")
        @Min(value = 1, message = "Stock quantity must be greater than zero")
        Integer quantity
) {
}