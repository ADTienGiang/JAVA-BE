package com.vannguyen.java_learn_ecom.modules.order.infrastructure.messaging;

import com.vannguyen.java_learn_ecom.modules.order.application.OrderEventPublisher;
import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderCreatedAfterCommitListener {

    private static final Logger log =
            LoggerFactory.getLogger(OrderCreatedAfterCommitListener.class);

    private final OrderEventPublisher orderEventPublisher;

    public OrderCreatedAfterCommitListener(
            OrderEventPublisher orderEventPublisher
    ) {
        this.orderEventPublisher = orderEventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreatedEvent event) {
        try {
            orderEventPublisher.publishOrderCreated(event);
        } catch (AmqpException exception) {
            log.error(
                    "Order committed but event publish failed: eventId={}, orderId={}",
                    event.eventId(),
                    event.orderId(),
                    exception
            );
        }
    }
}