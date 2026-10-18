package com.vannguyen.java_learn_ecom.modules.order.infrastructure.messaging;

import com.vannguyen.java_learn_ecom.modules.order.application.OrderEventPublisher;
import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitOrderEventPublisher implements OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitOrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                OrderRabbitConfig.ORDER_EXCHANGE,
                OrderRabbitConfig.ORDER_CREATED_ROUTING_KEY,
                event
        );
    }
}