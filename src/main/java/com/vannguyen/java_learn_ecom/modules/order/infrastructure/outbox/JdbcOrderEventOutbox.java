package com.vannguyen.java_learn_ecom.modules.order.infrastructure.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vannguyen.java_learn_ecom.modules.order.application.OrderEventOutbox;
import com.vannguyen.java_learn_ecom.modules.order.application.event.OrderCreatedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcOrderEventOutbox implements OrderEventOutbox {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public JdbcOrderEventOutbox(
            JdbcTemplate jdbcTemplate,
            ObjectMapper objectMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(OrderCreatedEvent event) {
        String payload;

        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Cannot serialize order event: " + event.eventId(),
                    exception
            );
        }

        jdbcTemplate.update(
                """
                INSERT INTO outbox_events (
                    event_id,
                    event_type,
                    payload
                )
                VALUES (?, ?, CAST(? AS JSONB))
                """,
                event.eventId(),
                "ORDER_CREATED",
                payload
        );
    }

    @Override
    public List<OrderCreatedEvent> findUnpublished(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }

        return jdbcTemplate.query(
                """
                SELECT event_id, payload
                FROM outbox_events
                WHERE published_at IS NULL
                  AND event_type = ?
                ORDER BY created_at ASC, event_id ASC
                LIMIT ?
                """,
                (resultSet, rowNum) -> {
                    String payload = resultSet.getString("payload");

                    try {
                        return objectMapper.readValue(
                                payload,
                                OrderCreatedEvent.class
                        );
                    } catch (JsonProcessingException exception) {
                        throw new IllegalStateException(
                                "Cannot deserialize outbox event: "
                                        + resultSet.getString("event_id"),
                                exception
                        );
                    }
                },
                "ORDER_CREATED",
                limit
        );
    }

    @Override
    @Transactional
    public void markPublished(UUID eventId) {
        jdbcTemplate.update(
                """
                UPDATE outbox_events
                SET published_at = NOW()
                WHERE event_id = ?
                  AND published_at IS NULL
                """,
                eventId
        );
    }
}