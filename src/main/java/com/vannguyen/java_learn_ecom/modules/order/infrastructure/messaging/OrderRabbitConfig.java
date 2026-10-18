package com.vannguyen.java_learn_ecom.modules.order.infrastructure.messaging;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class OrderRabbitConfig {

    public static final String ORDER_EXCHANGE = "ecom.order.exchange";
    public static final String ORDER_CREATED_EMAIL_QUEUE =
            "ecom.order.created.email.queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

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
}