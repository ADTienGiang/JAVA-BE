package com.vannguyen.java_learn_ecom.modules.order.infrastructure.messaging;

import com.vannguyen.java_learn_ecom.modules.order.application.OrderEventPublisher;
import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitOrderEventPublisher implements OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitOrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        CorrelationData correlationData = new CorrelationData();

        rabbitTemplate.convertAndSend(
                OrderRabbitConfig.ORDER_EXCHANGE,
                OrderRabbitConfig.ORDER_CREATED_ROUTING_KEY,
                event,
                correlationData
        );

        CorrelationData.Confirm confirm;

        try {
            confirm = correlationData.getFuture()
                    .get(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            throw new AmqpException(
                    "Interrupted while waiting for confirm: " + event.eventId(),
                    exception
            );
        } catch (TimeoutException exception) {
            throw new AmqpException(
                    "Timed out waiting for confirm: " + event.eventId(),
                    exception
            );
        } catch (ExecutionException exception) {
            throw new AmqpException(
                    "Failed to receive confirm: " + event.eventId(),
                    exception
            );
        }

        if (!confirm.ack()) {
            throw new AmqpException(
                    "Publish was not acknowledged: " + event.eventId()
                            + ", reason=" + confirm.reason()
            );
        }

        if (correlationData.getReturned() != null) {
            throw new AmqpException(
                    "Message could not be routed to a queue: "
                            + event.eventId()
            );
        }
    }
}