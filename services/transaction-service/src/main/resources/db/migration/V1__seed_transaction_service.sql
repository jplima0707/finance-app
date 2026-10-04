-- Baseline schema and deterministic seed data for the transaction service.
-- Every INSERT is safe to execute more than once because it uses a stable
-- primary/unique key and ON CONFLICT DO NOTHING.

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    phone VARCHAR(20),
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    birth_date DATE
);

CREATE TABLE IF NOT EXISTS accounts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CONSTRAINT accounts_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id UUID PRIMARY KEY,
    source_account_id UUID NOT NULL,
    destination_account_id UUID,
    amount NUMERIC(19, 2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    idempotency_key VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT transactions_source_account_fk FOREIGN KEY (source_account_id) REFERENCES accounts (id),
    CONSTRAINT transactions_destination_account_fk FOREIGN KEY (destination_account_id) REFERENCES accounts (id)
);

INSERT INTO users (id, name, phone, cpf, email, created_at, birth_date)
VALUES
    ('11111111-1111-4111-8111-111111111111', 'Ana Souza', '+5511999990001', '529.982.247-25', 'ana.souza@example.com', '2026-01-15T10:00:00Z', '1990-04-12'),
    ('22222222-2222-4222-8222-222222222222', 'Bruno Lima', '+5511999990002', '111.444.777-35', 'bruno.lima@example.com', '2026-01-15T10:05:00Z', '1988-09-23'),
    ('33333333-3333-4333-8333-333333333333', 'Carla Mendes', '+5511999990003', '935.411.347-80', 'carla.mendes@example.com', '2026-01-15T10:10:00Z', '1995-02-08')
ON CONFLICT DO NOTHING;

INSERT INTO accounts (id, user_id, status, created_at, updated_at)
VALUES
    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', '11111111-1111-4111-8111-111111111111', 'ACTIVE', '2026-01-15T10:15:00Z', '2026-01-15T10:15:00Z'),
    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb', '22222222-2222-4222-8222-222222222222', 'ACTIVE', '2026-01-15T10:16:00Z', '2026-01-15T10:16:00Z'),
    ('cccccccc-cccc-4ccc-8ccc-cccccccccccc', '33333333-3333-4333-8333-333333333333', 'INACTIVE', '2026-01-15T10:17:00Z', '2026-01-15T10:17:00Z')
ON CONFLICT DO NOTHING;

INSERT INTO transactions (transaction_id, source_account_id, destination_account_id, amount, status, description, created_at, idempotency_key)
VALUES
    ('dddddddd-dddd-4ddd-8ddd-dddddddddddd', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb', 150.00, 'COMPLETED', 'Seed transfer from Ana to Bruno', '2026-01-15T11:00:00Z', 'seed-transfer-ana-bruno-001'),
    ('eeeeeeee-eeee-4eee-8eee-eeeeeeeeeeee', 'bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', 50.00, 'COMPLETED', 'Seed transfer from Bruno to Ana', '2026-01-15T11:05:00Z', 'seed-transfer-bruno-ana-001')
ON CONFLICT DO NOTHING;
