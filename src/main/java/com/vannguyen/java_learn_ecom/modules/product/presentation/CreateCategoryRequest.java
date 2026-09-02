package com.vannguyen.java_learn_ecom.modules.product.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(

        @NotBlank(message = "Category name must not be blank")
        @Size(max = 100, message = "Category name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Category slug must not be blank")
        @Size(max = 120, message = "Category slug must not exceed 120 characters")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Category slug must contain only lowercase letters, numbers, and hyphens"
        )
        String slug
) {
}