create table categories (
                            id bigserial primary key,
                            name varchar(100) not null,
                            slug varchar(120) not null,
                            active boolean not null default true,
                            created_at timestamp not null default current_timestamp,
                            updated_at timestamp not null default current_timestamp,

                            constraint uk_categories_slug unique (slug)
);

create table products (
                          id bigserial primary key,
                          category_id bigint not null,
                          name varchar(150) not null,
                          description varchar(2000) not null,
                          price numeric(19, 2) not null,
                          status varchar(30) not null,
                          created_at timestamp not null default current_timestamp,
                          updated_at timestamp not null default current_timestamp,

                          constraint fk_products_category
                              foreign key (category_id)
                                  references categories (id),

                          constraint ck_products_price_positive
                              check (price > 0)
);

create table product_variants (
                                  id bigserial primary key,
                                  product_id bigint not null,
                                  sku varchar(80) not null,
                                  name varchar(150) not null,
                                  price numeric(19, 2) not null,
                                  active boolean not null default true,
                                  created_at timestamp not null default current_timestamp,
                                  updated_at timestamp not null default current_timestamp,

                                  constraint fk_product_variants_product
                                      foreign key (product_id)
                                          references products (id),

                                  constraint uk_product_variants_sku unique (sku),

                                  constraint ck_product_variants_price_positive
                                      check (price > 0)
);

create index idx_products_category_id on products (category_id);
create index idx_products_status on products (status);
create index idx_product_variants_product_id on product_variants (product_id);