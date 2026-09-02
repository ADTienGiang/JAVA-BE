package com.vannguyen.java_learn_ecom.modules.product.presentation;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        boolean active
) {
}