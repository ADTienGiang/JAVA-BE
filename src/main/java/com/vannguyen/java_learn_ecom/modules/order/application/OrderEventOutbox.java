package com.vannguyen.java_learn_ecom.modules.order.application;

import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;

import java.util.List;
import java.util.UUID;
public interface OrderEventOutbox {

    void save(OrderCreatedEvent event);

    List<OrderCreatedEvent> findUnpublished(int limit);

    void markPublished(UUID eventId);
}