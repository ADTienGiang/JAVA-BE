package com.vannguyen.java_learn_ecom.modules.product.application;

public record UpdateCategoryCommand(
        Long id,
        String name,
        String slug
) {
}