package com.vannguyen.java_learn_ecom.common.infrastructure.messaging;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public class JdbcProcessedEventRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcProcessedEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean tryRecord(String consumerName, UUID eventId) {
        int insertedRows = jdbcTemplate.update(
                """
                INSERT INTO processed_events (consumer_name, event_id)
                VALUES (?, ?)
                ON CONFLICT (consumer_name, event_id) DO NOTHING
                """,
                consumerName,
                eventId
        );

        return insertedRows == 1;
    }
}