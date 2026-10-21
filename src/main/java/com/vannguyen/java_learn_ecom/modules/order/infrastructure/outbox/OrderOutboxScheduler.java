package com.vannguyen.java_learn_ecom.modules.order.infrastructure.outbox;

import com.vannguyen.java_learn_ecom.modules.order.application.PublishOrderOutboxUseCase;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class OrderOutboxScheduler {

    private final PublishOrderOutboxUseCase publishOrderOutboxUseCase;

    public OrderOutboxScheduler(
            PublishOrderOutboxUseCase publishOrderOutboxUseCase
    ) {
        this.publishOrderOutboxUseCase = publishOrderOutboxUseCase;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void publishPendingEvents() {
        publishOrderOutboxUseCase.publishPending();
    }
}