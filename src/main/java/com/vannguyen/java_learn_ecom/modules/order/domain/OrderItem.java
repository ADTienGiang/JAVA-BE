package com.vannguyen.java_learn_ecom.modules.order.domain;

import java.math.BigDecimal;

public class OrderItem {

    private final Long id;
    private final Long productId;
    private final Long variantId;
    private final String productName;
    private final String variantName;
    private final BigDecimal unitPrice;
    private final int quantity;

    public OrderItem(
            Long id,
            Long productId,
            Long variantId,
            String productName,
            String variantName,
            BigDecimal unitPrice,
            int quantity
    ) {
        validateId(productId, "Product id must not be null");
        validateId(variantId, "Variant id must not be null");
        validateText(productName, "Product name must not be blank");
        validateText(variantName, "Variant name must not be blank");
        validateUnitPrice(unitPrice);
        validateQuantity(quantity);

        this.id = id;
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName.trim();
        this.variantName = variantName.trim();
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public String getProductName() {
        return productName;
    }

    public String getVariantName() {
        return variantName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    private void validateId(Long id, String message) {
        if (id == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Order item quantity must be greater than zero");
        }
    }
}