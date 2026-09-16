package com.vannguyen.java_learn_ecom.modules.order.presentation;

import com.vannguyen.java_learn_ecom.modules.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        OrderStatus status,
        BigDecimal totalAmount,
        ShippingAddressResponse shippingAddress,
        List<OrderItemResponse> items
) {
}