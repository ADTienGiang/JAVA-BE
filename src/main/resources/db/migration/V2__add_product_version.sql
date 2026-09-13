alter table products
    add column if not exists version bigint not null default 0;