create table product_variant_stocks (
                                        id bigserial primary key,
                                        variant_id bigint not null,
                                        quantity integer not null,
                                        available boolean not null default true,
                                        created_at timestamp not null default current_timestamp,
                                        updated_at timestamp not null default current_timestamp,

                                        constraint fk_product_variant_stocks_variant
                                            foreign key (variant_id)
                                                references product_variants (id),

                                        constraint uk_product_variant_stocks_variant_id
                                            unique (variant_id),

                                        constraint ck_product_variant_stocks_quantity_non_negative
                                            check (quantity >= 0)
);

create index idx_product_variant_stocks_available
    on product_variant_stocks (available);