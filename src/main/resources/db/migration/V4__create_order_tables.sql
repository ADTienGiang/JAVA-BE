create table orders (
                        id bigserial primary key,
                        customer_id bigint not null,
                        status varchar(30) not null,
                        total_amount numeric(19, 2) not null,

                        receiver_name varchar(150) not null,
                        phone varchar(30) not null,
                        address_line varchar(500) not null,
                        ward varchar(120) not null,
                        district varchar(120) not null,
                        city varchar(120) not null,

                        created_at timestamp not null default current_timestamp,
                        updated_at timestamp not null default current_timestamp,

                        constraint ck_orders_total_amount_positive
                            check (total_amount > 0)
);

create table order_items (
                             id bigserial primary key,
                             order_id bigint not null,
                             product_id bigint not null,
                             variant_id bigint not null,

                             product_name varchar(150) not null,
                             variant_name varchar(150) not null,
                             unit_price numeric(19, 2) not null,
                             quantity integer not null,
                             line_total numeric(19, 2) not null,

                             created_at timestamp not null default current_timestamp,
                             updated_at timestamp not null default current_timestamp,

                             constraint fk_order_items_order
                                 foreign key (order_id)
                                     references orders (id),

                             constraint fk_order_items_product
                                 foreign key (product_id)
                                     references products (id),

                             constraint fk_order_items_variant
                                 foreign key (variant_id)
                                     references product_variants (id),

                             constraint ck_order_items_unit_price_positive
                                 check (unit_price > 0),

                             constraint ck_order_items_quantity_positive
                                 check (quantity > 0),

                             constraint ck_order_items_line_total_positive
                                 check (line_total > 0)
);

create index idx_orders_customer_id
    on orders (customer_id);

create index idx_orders_status
    on orders (status);

create index idx_order_items_order_id
    on order_items (order_id);

create index idx_order_items_variant_id
    on order_items (variant_id);