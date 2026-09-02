package com.vannguyen.java_learn_ecom.modules.product.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequest(

        @NotNull(message = "Product category id must not be null")
        Long categoryId,

        @NotBlank(message = "Product name must not be blank")
        @Size(max = 150, message = "Product name must not exceed 150 characters")
        String name,

        @NotBlank(message = "Product description must not be blank")
        @Size(max = 2000, message = "Product description must not exceed 2000 characters")
        String description,

        @NotNull(message = "Product price must not be null")
        @Positive(message = "Product price must be greater than zero")
        BigDecimal price
) {
}