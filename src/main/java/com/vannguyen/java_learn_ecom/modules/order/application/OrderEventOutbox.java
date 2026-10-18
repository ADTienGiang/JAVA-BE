package com.vannguyen.java_learn_ecom.modules.order.application;

import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;

public interface OrderEventOutbox {

    void save(OrderCreatedEvent event);
}