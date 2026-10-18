package com.vannguyen.java_learn_ecom.modules.order.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;
@Component
public class OrderCreatedEmailConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderCreatedEmailConsumer.class);

    @RabbitListener(queues = OrderRabbitConfig.ORDER_CREATED_EMAIL_QUEUE)
    public void handle(OrderCreatedEvent event) {
        log.info(
                "Email consumer received: eventId={}, orderId={}, customerId={}, occurredAt={}",
                event.eventId(),
                event.orderId(),
                event.customerId(),
                event.occurredAt()
        );
    }
}