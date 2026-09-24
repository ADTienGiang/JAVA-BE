CREATE TABLE user_accounts (
                               id BIGSERIAL PRIMARY KEY,
                               email VARCHAR(255) NOT NULL,
                               password_hash VARCHAR(255) NOT NULL,
                               role VARCHAR(50) NOT NULL,
                               active BOOLEAN NOT NULL DEFAULT TRUE,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                               CONSTRAINT uk_user_accounts_email UNIQUE (email),
                               CONSTRAINT ck_user_accounts_role CHECK (role IN ('CUSTOMER', 'ADMIN'))
);