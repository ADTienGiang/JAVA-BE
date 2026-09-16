package com.vannguyen.java_learn_ecom.modules.order.application;

import com.vannguyen.java_learn_ecom.common.exception.ResourceNotFoundException;
import com.vannguyen.java_learn_ecom.modules.order.domain.Order;
import com.vannguyen.java_learn_ecom.modules.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final StockGateway stockGateway;

    public CancelOrderUseCase(
            OrderRepository orderRepository,
            StockGateway stockGateway
    ) {
        this.orderRepository = orderRepository;
        this.stockGateway = stockGateway;
    }

    @Transactional
    public Order cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.cancel();

        order.getItems().forEach(item -> stockGateway.restore(
                item.getProductId(),
                item.getVariantId(),
                item.getQuantity()
        ));

        return orderRepository.save(order);
    }
}