package com.vannguyen.java_learn_ecom.modules.product.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductVariantRequest(

        @NotBlank(message = "Product variant sku must not be blank")
        @Size(max = 80, message = "Product variant sku must not exceed 80 characters")
        String sku,

        @NotBlank(message = "Product variant name must not be blank")
        @Size(max = 150, message = "Product variant name must not exceed 150 characters")
        String name,

        @NotNull(message = "Product variant price must not be null")
        @Positive(message = "Product variant price must be greater than zero")
        BigDecimal price
) {
}