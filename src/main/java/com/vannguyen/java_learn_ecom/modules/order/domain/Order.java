package com.vannguyen.java_learn_ecom.modules.order.domain;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private final Long id;
    private final Long customerId;
    private final List<OrderItem> items;
    private final ShippingAddress shippingAddress;
    private OrderStatus status;

    public Order(
            Long id,
            Long customerId,
            List<OrderItem> items,
            ShippingAddress shippingAddress,
            OrderStatus status
    ) {
        validateCustomerId(customerId);
        validateItems(items);
        validateShippingAddress(shippingAddress);

        this.id = id;
        this.customerId = customerId;
        this.items = List.copyOf(items);
        this.shippingAddress = shippingAddress;
        this.status = status == null ? OrderStatus.PENDING : status;
    }

    public static Order create(
            Long customerId,
            List<OrderItem> items,
            ShippingAddress shippingAddress
    ) {
        return new Order(
                null,
                customerId,
                items,
                shippingAddress,
                OrderStatus.PENDING
        );
    }

    public void cancel() {
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }

        this.status = OrderStatus.CANCELLED;
    }

    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    private void validateCustomerId(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Order customer id must not be null");
        }
    }

    private void validateItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
    }

    private void validateShippingAddress(ShippingAddress shippingAddress) {
        if (shippingAddress == null) {
            throw new IllegalArgumentException("Shipping address must not be null");
        }
    }
}