-- V1__initial_schema.sql

CREATE TABLE users (
    id VARCHAR(128) PRIMARY KEY, -- Firebase UID
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE budgets (
    id VARCHAR(36) PRIMARY KEY, -- L'ID généré par le mobile
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    monthly_income_in_cents INTEGER NOT NULL DEFAULT 0,
    server_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE outgoings (
    id VARCHAR(36) PRIMARY KEY, -- L'ID généré par le mobile
    budget_id VARCHAR(36) NOT NULL REFERENCES budgets(id) ON DELETE CASCADE,
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    amount_in_cents INTEGER NOT NULL,
    recurrence VARCHAR(50) NOT NULL,
    due_day INTEGER NOT NULL,
    due_month INTEGER, -- Optionnel (null pour mensuel)

    -- Timestamps pour la synchro
    created_at TIMESTAMP WITH TIME ZONE NOT NULL, -- Date de création sur le mobile
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL, -- Date de modif sur le mobile
    server_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_outgoings_server_sync ON outgoings(user_id, server_updated_at);