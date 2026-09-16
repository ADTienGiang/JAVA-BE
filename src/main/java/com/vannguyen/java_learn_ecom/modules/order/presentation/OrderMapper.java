package com.vannguyen.java_learn_ecom.modules.order.presentation;

import com.vannguyen.java_learn_ecom.modules.order.application.CreateOrderCommand;
import com.vannguyen.java_learn_ecom.modules.order.application.CreateOrderItemCommand;
import com.vannguyen.java_learn_ecom.modules.order.application.CreateShippingAddressCommand;
import com.vannguyen.java_learn_ecom.modules.order.domain.Order;
import com.vannguyen.java_learn_ecom.modules.order.domain.OrderItem;
import com.vannguyen.java_learn_ecom.modules.order.domain.ShippingAddress;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public CreateOrderCommand toCommand(CreateOrderRequest request) {
        return new CreateOrderCommand(
                request.customerId(),
                request.items()
                        .stream()
                        .map(this::toItemCommand)
                        .toList(),
                toShippingAddressCommand(request.shippingAddress())
        );
    }

    public OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                toShippingAddressResponse(order.getShippingAddress()),
                order.getItems()
                        .stream()
                        .map(this::toItemResponse)
                        .toList()
        );
    }

    private CreateOrderItemCommand toItemCommand(CreateOrderItemRequest request) {
        return new CreateOrderItemCommand(
                request.productId(),
                request.variantId(),
                request.quantity()
        );
    }

    private CreateShippingAddressCommand toShippingAddressCommand(ShippingAddressRequest request) {
        return new CreateShippingAddressCommand(
                request.receiverName(),
                request.phone(),
                request.addressLine(),
                request.ward(),
                request.district(),
                request.city()
        );
    }

    private ShippingAddressResponse toShippingAddressResponse(ShippingAddress address) {
        return new ShippingAddressResponse(
                address.receiverName(),
                address.phone(),
                address.addressLine(),
                address.ward(),
                address.district(),
                address.city()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getVariantId(),
                item.getProductName(),
                item.getVariantName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }
}