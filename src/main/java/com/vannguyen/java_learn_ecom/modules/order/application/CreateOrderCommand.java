package com.vannguyen.java_learn_ecom.modules.order.application;

import java.util.List;

public record CreateOrderCommand(
        Long customerId,
        List<CreateOrderItemCommand> items,
        CreateShippingAddressCommand shippingAddress
) {
}