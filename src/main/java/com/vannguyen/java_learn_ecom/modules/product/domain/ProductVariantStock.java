package com.vannguyen.java_learn_ecom.modules.product.domain;

public class ProductVariantStock {

    private final Long id;
    private final Long variantId;
    private int quantity;
    private boolean available;

    public ProductVariantStock(
            Long id,
            Long variantId,
            int quantity,
            boolean available
    ) {
        validateVariantId(variantId);
        validateQuantity(quantity);

        this.id = id;
        this.variantId = variantId;
        this.quantity = quantity;
        this.available = available;
    }

    public static ProductVariantStock initialize(Long variantId, int quantity) {
        return new ProductVariantStock(
                null,
                variantId,
                quantity,
                true
        );
    }

    public Long getId() {
        return id;
    }

    public Long getVariantId() {
        return variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markAvailable() {
        this.available = true;
    }

    public void markUnavailable() {
        this.available = false;
    }

    private void validateVariantId(Long variantId) {
        if (variantId == null) {
            throw new IllegalArgumentException("Product variant stock variant id must not be null");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Product variant stock quantity must not be negative");
        }
    }
}