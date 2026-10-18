package com.vannguyen.java_learn_ecom.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;

import java.time.Instant;
import java.util.UUID;
import com.vannguyen.java_learn_ecom.modules.order.application.OrderEventPublisher;

public class RabbitConnectionCheck implements ApplicationRunner {

    private static final Logger log =
            LoggerFactory.getLogger(RabbitConnectionCheck.class);

    private final OrderEventPublisher orderEventPublisher;

    public RabbitConnectionCheck(OrderEventPublisher orderEventPublisher) {
        this.orderEventPublisher = orderEventPublisher;
    }

    @Override
    public void run(ApplicationArguments args) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                123L,
                10L,
                Instant.now()
        );

        orderEventPublisher.publishOrderCreated(event);

        log.info("Publish call completed: eventId={}, orderId={}",
                event.eventId(),
                event.orderId());
    }
}