package com.vannguyen.java_learn_ecom.modules.product.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Product name is required")
        @Size(max = 100, message = "Product name must be at most 100 characters")
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than 0")
        BigDecimal price
) {
}