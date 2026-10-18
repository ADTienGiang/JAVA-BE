CREATE TABLE outbox_events (
                               event_id UUID PRIMARY KEY,
                               event_type VARCHAR(100) NOT NULL,
                               payload JSONB NOT NULL,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               published_at TIMESTAMPTZ
);

CREATE INDEX idx_outbox_events_unpublished
    ON outbox_events (created_at, event_id)
    WHERE published_at IS NULL;