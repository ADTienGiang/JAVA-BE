package com.vannguyen.java_learn_ecom.modules.order.infrastructure.messaging;

import com.vannguyen.java_learn_ecom.common.infrastructure.messaging.JdbcProcessedEventRepository;
import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderCreatedEmailConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderCreatedEmailConsumer.class);

    private static final String CONSUMER_NAME = "order-created-email";

    private final JdbcProcessedEventRepository processedEventRepository;

    public OrderCreatedEmailConsumer(
            JdbcProcessedEventRepository processedEventRepository
    ) {
        this.processedEventRepository = processedEventRepository;
    }

    @RabbitListener(queues = OrderRabbitConfig.ORDER_CREATED_EMAIL_QUEUE)
    @Transactional
    public void handle(OrderCreatedEvent event) {
        boolean firstProcessing = processedEventRepository.tryRecord(
                CONSUMER_NAME,
                event.eventId()
        );

        if (!firstProcessing) {
            log.info(
                    "Duplicate event skipped: consumer={}, eventId={}",
                    CONSUMER_NAME,
                    event.eventId()
            );
            return;
        }

        log.info(
                "Simulating order email: eventId={}, orderId={}, customerId={}",
                event.eventId(),
                event.orderId(),
                event.customerId()
        );
    }
}