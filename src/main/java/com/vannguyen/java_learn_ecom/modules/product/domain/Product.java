package com.vannguyen.java_learn_ecom.modules.product.domain;

import java.math.BigDecimal;

public class Product {

    private final Long id;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductStatus status;

    public Product(
            Long id,
            Long categoryId,
            String name,
            String description,
            BigDecimal price,
            ProductStatus status
    ) {
        validateCategoryId(categoryId);
        validateName(name);
        validateDescription(description);
        validatePrice(price);

        this.id = id;
        this.categoryId = categoryId;
        this.name = name.trim();
        this.description = description.trim();
        this.price = price;
        this.status = status == null ? ProductStatus.ACTIVE : status;
    }

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void update(
            Long categoryId,
            String name,
            String description,
            BigDecimal price
    ) {
        validateCategoryId(categoryId);
        validateName(name);
        validateDescription(description);
        validatePrice(price);

        this.categoryId = categoryId;
        this.name = name.trim();
        this.description = description.trim();
        this.price = price;
    }

    public void activate() {
        this.status = ProductStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }

    private void validateCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Product category id must not be null");
        }
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Product name must not be blank");
        }
    }

    private void validateDescription(String description) {
        if (isBlank(description)) {
            throw new IllegalArgumentException("Product description must not be blank");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}