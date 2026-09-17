alter table orders
    add column cancelled_at timestamp;

alter table orders
    add column cancel_reason varchar(500);