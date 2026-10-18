package com.vannguyen.java_learn_ecom.modules.order.application.event;

import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        Long orderId,
        Long customerId,
        Instant occurredAt
) {
}