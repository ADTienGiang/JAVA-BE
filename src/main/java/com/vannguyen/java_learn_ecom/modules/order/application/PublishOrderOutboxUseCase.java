package com.vannguyen.java_learn_ecom.modules.order.application;

import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishOrderOutboxUseCase {

    private static final Logger log =
            LoggerFactory.getLogger(PublishOrderOutboxUseCase.class);

    private final OrderEventOutbox orderEventOutbox;
    private final OrderEventPublisher orderEventPublisher;

    public PublishOrderOutboxUseCase(
            OrderEventOutbox orderEventOutbox,
            OrderEventPublisher orderEventPublisher
    ) {
        this.orderEventOutbox = orderEventOutbox;
        this.orderEventPublisher = orderEventPublisher;
    }

    public void publishPending() {
        List<OrderCreatedEvent> events =
                orderEventOutbox.findUnpublished(20);

        for (OrderCreatedEvent event : events) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            try {
                orderEventPublisher.publishOrderCreated(event);

                orderEventOutbox.markPublished(event.eventId());

                log.info(
                        "Outbox event published: eventId={}, orderId={}",
                        event.eventId(),
                        event.orderId()
                );
            } catch (RuntimeException exception) {
                log.error(
                        "Outbox publish attempt failed: eventId={}, orderId={}",
                        event.eventId(),
                        event.orderId(),
                        exception
                );

                return;
            }
        }
    }
}