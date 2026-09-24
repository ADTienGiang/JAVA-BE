package com.vannguyen.java_learn_ecom.modules.order.infrastructure;

import com.vannguyen.java_learn_ecom.modules.order.domain.Order;
import com.vannguyen.java_learn_ecom.modules.order.domain.OrderItem;
import com.vannguyen.java_learn_ecom.modules.order.domain.ShippingAddress;
import org.springframework.stereotype.Component;

@Component
public class OrderJpaMapper {

    public OrderJpaEntity toEntity(Order order) {
        ShippingAddress address = order.getShippingAddress();

        OrderJpaEntity entity = new OrderJpaEntity(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                address.receiverName(),
                address.phone(),
                address.addressLine(),
                address.ward(),
                address.district(),
                address.city(),
                order.getSource()
        );

        order.getItems()
                .stream()
                .map(this::toItemEntity)
                .forEach(entity::addItem);

        return entity;
    }

    public Order toDomain(OrderJpaEntity entity) {
        ShippingAddress address = new ShippingAddress(
                entity.getReceiverName(),
                entity.getPhone(),
                entity.getAddressLine(),
                entity.getWard(),
                entity.getDistrict(),
                entity.getCity()
        );

        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                entity.getItems()
                        .stream()
                        .map(this::toItemDomain)
                        .toList(),
                address,
                entity.getStatus()
        );
    }

    private OrderItemJpaEntity toItemEntity(OrderItem item) {
        return new OrderItemJpaEntity(
                item.getId(),
                item.getProductId(),
                item.getVariantId(),
                item.getProductName(),
                item.getVariantName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }

    private OrderItem toItemDomain(OrderItemJpaEntity entity) {
        return new OrderItem(
                entity.getId(),
                entity.getProductId(),
                entity.getVariantId(),
                entity.getProductName(),
                entity.getVariantName(),
                entity.getUnitPrice(),
                entity.getQuantity()
        );
    }
}