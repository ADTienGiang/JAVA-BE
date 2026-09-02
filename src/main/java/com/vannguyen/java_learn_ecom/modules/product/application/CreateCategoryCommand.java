package com.vannguyen.java_learn_ecom.modules.product.application;

public record CreateCategoryCommand(
        String name,
        String slug
) {
}