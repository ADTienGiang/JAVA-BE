package com.vannguyen.java_learn_ecom.modules.order.application;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.modules.order.domain.Order;
import com.vannguyen.java_learn_ecom.modules.order.domain.OrderItem;
import com.vannguyen.java_learn_ecom.modules.order.domain.OrderRepository;
import com.vannguyen.java_learn_ecom.modules.order.domain.ShippingAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;

import java.time.Instant;
import java.util.UUID;


@Service
public class CreateOrderUseCase {

    private final ProductCatalogGateway productCatalogGateway;
    private final StockGateway stockGateway;
    private final OrderRepository orderRepository;
    private final OrderEventOutbox orderEventOutbox;

    public CreateOrderUseCase(
            ProductCatalogGateway productCatalogGateway,
            StockGateway stockGateway,
            OrderRepository orderRepository,
            OrderEventOutbox orderEventOutbox
    ) {
        this.productCatalogGateway = productCatalogGateway;
        this.stockGateway = stockGateway;
        this.orderRepository = orderRepository;
        this.orderEventOutbox = orderEventOutbox;
    }

    @Transactional
    public Order create(CreateOrderCommand command) {
        validateCommand(command);

        List<OrderItem> orderItems = command.items()
                .stream()
                .map(this::createOrderItem)
                .toList();

        ShippingAddress shippingAddress = toShippingAddress(command.shippingAddress());

        Order order = Order.create(
                command.customerId(),
                orderItems,
                shippingAddress
        );

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                Instant.now()
        );

        orderEventOutbox.save(event);


        return savedOrder;
    }

    private OrderItem createOrderItem(CreateOrderItemCommand itemCommand) {
        ProductItemSnapshot productItem = productCatalogGateway.getSellableProductItem(
                itemCommand.productId(),
                itemCommand.variantId()
        );

        stockGateway.decrease(
                itemCommand.productId(),
                itemCommand.variantId(),
                itemCommand.quantity()
        );

        return new OrderItem(
                null,
                productItem.productId(),
                productItem.variantId(),
                productItem.productName(),
                productItem.variantName(),
                productItem.unitPrice(),
                itemCommand.quantity()
        );
    }

    private ShippingAddress toShippingAddress(CreateShippingAddressCommand command) {
        return new ShippingAddress(
                command.receiverName(),
                command.phone(),
                command.addressLine(),
                command.ward(),
                command.district(),
                command.city()
        );
    }

    private void validateCommand(CreateOrderCommand command) {
        if (command == null) {
            throw new BusinessException("Create order command must not be null");
        }

        if (command.items() == null || command.items().isEmpty()) {
            throw new BusinessException("Order must have at least one item");
        }

        if (command.shippingAddress() == null) {
            throw new BusinessException("Shipping address must not be null");
        }
    }
}