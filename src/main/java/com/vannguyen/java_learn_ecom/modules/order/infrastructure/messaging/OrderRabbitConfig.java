package com.vannguyen.java_learn_ecom.modules.order.infrastructure.messaging;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
@Configuration
public class OrderRabbitConfig {

    public static final String ORDER_EXCHANGE = "ecom.order.exchange";
    public static final String ORDER_CREATED_EMAIL_QUEUE =
            "ecom.order.created.email.queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    public static final String ORDER_DLX = "ecom.order.dlx";

    public static final String ORDER_CREATED_EMAIL_DLQ =
            "ecom.order.created.email.dlq";

    public static final String ORDER_CREATED_EMAIL_FAILED_ROUTING_KEY =
            "order.created.email.failed";
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(
                ORDER_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Queue orderCreatedEmailQueue() {
        return new Queue(
                ORDER_CREATED_EMAIL_QUEUE,
                true,
                false,
                false
        );
    }

    @Bean
    public Binding orderCreatedEmailBinding(
            Queue orderCreatedEmailQueue,
            DirectExchange orderExchange
    ) {
        return BindingBuilder
                .bind(orderCreatedEmailQueue)
                .to(orderExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public DirectExchange orderDeadLetterExchange() {
        return new DirectExchange(
                ORDER_DLX,
                true,
                false
        );
    }

    @Bean
    public Queue orderCreatedEmailDlq() {
        return new Queue(
                ORDER_CREATED_EMAIL_DLQ,
                true,
                false,
                false
        );
    }

    @Bean
    public Binding orderCreatedEmailDlqBinding(
            @Qualifier("orderCreatedEmailDlq") Queue dlq,
            @Qualifier("orderDeadLetterExchange") DirectExchange dlx
    ) {
        return BindingBuilder
                .bind(dlq)
                .to(dlx)
                .with(ORDER_CREATED_EMAIL_FAILED_ROUTING_KEY);
    }
}