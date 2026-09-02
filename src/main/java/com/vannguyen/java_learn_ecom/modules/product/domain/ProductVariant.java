package com.vannguyen.java_learn_ecom.modules.product.domain;

import java.math.BigDecimal;

public class ProductVariant {

    private final Long id;
    private final Long productId;
    private String sku;
    private String name;
    private BigDecimal price;
    private boolean active;

    public ProductVariant(
            Long id,
            Long productId,
            String sku,
            String name,
            BigDecimal price,
            boolean active
    ) {
        validateProductId(productId);
        validateSku(sku);
        validateName(name);
        validatePrice(price);

        this.id = id;
        this.productId = productId;
        this.sku = sku.trim().toUpperCase();
        this.name = name.trim();
        this.price = price;
        this.active = active;
    }

    public static ProductVariant create(
            Long productId,
            String sku,
            String name,
            BigDecimal price
    ) {
        return new ProductVariant(
                null,
                productId,
                sku,
                name,
                price,
                true
        );
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    public void update(
            String sku,
            String name,
            BigDecimal price
    ) {
        validateSku(sku);
        validateName(name);
        validatePrice(price);

        this.sku = sku.trim().toUpperCase();
        this.name = name.trim();
        this.price = price;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product variant product id must not be null");
        }
    }

    private void validateSku(String sku) {
        if (isBlank(sku)) {
            throw new IllegalArgumentException("Product variant sku must not be blank");
        }
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Product variant name must not be blank");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product variant price must be greater than zero");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}