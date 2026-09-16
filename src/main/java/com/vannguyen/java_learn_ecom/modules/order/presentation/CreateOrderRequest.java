package com.vannguyen.java_learn_ecom.modules.order.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "Customer id must not be null")
        Long customerId,

        @Valid
        @NotEmpty(message = "Order must have at least one item")
        List<CreateOrderItemRequest> items,

        @Valid
        @NotNull(message = "Shipping address must not be null")
        ShippingAddressRequest shippingAddress
) {
}