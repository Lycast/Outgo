-- V1__initial_schema.sql (Mise à jour Phase 3)

CREATE TABLE users (
    id VARCHAR(128) PRIMARY KEY, -- Firebase UID
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wallets (
    id VARCHAR(36) PRIMARY KEY, -- UUID généré par le mobile
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,

    -- Timestamps pour la synchro
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE, -- null = actif
    server_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE operations (
    id VARCHAR(36) PRIMARY KEY, -- UUID généré par le mobile
    wallet_id VARCHAR(36) NOT NULL REFERENCES wallets(id) ON DELETE CASCADE,
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    amount_in_cents BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL, -- INCOME / EXPENSE
    recurrence VARCHAR(50) NOT NULL,

    -- Moteur temporel
    start_date BIGINT NOT NULL, -- On stocke l'EpochMillis en BIGINT pour plus de simplicité
    end_date BIGINT,

    -- Timestamps pour la synchro
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE,
    server_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,

    -- Statut : 'active', 'cancelled', 'expired'
    status VARCHAR(20) NOT NULL DEFAULT 'active',

    -- Source : 'google_play', 'app_store', 'stripe_web'
    source VARCHAR(20) NOT NULL,

    -- ID de transaction original (pour le support et la vérification)
    external_transaction_id VARCHAR(255) NOT NULL,

    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Index crucial pour la performance du PULL
CREATE INDEX idx_operations_server_sync ON operations(user_id, server_updated_at);
CREATE INDEX idx_wallets_server_sync ON wallets(user_id, server_updated_at);
CREATE INDEX idx_user_subscription_active ON subscriptions(user_id, end_date)
    WHERE status = 'active';