-- Baseline schema and deterministic seed data for the ledger service.
-- Ledger IDs are fixed so this script is idempotent when re-applied.

CREATE TABLE IF NOT EXISTS ledger_entries (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL,
    account_id UUID NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    type VARCHAR(255),
    created_at TIMESTAMP(6) WITH TIME ZONE
);

INSERT INTO ledger_entries (id, transaction_id, account_id, amount, type, created_at)
VALUES
    ('f1111111-1111-4111-8111-111111111111', 'dddddddd-dddd-4ddd-8ddd-dddddddddddd', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', 150.00, 'DEBIT', '2026-01-15T11:00:01Z'),
    ('f2222222-2222-4222-8222-222222222222', 'dddddddd-dddd-4ddd-8ddd-dddddddddddd', 'bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb', 150.00, 'CREDIT', '2026-01-15T11:00:01Z'),
    ('f3333333-3333-4333-8333-333333333333', 'eeeeeeee-eeee-4eee-8eee-eeeeeeeeeeee', 'bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb', 50.00, 'DEBIT', '2026-01-15T11:05:01Z'),
    ('f4444444-4444-4444-8444-444444444444', 'eeeeeeee-eeee-4eee-8eee-eeeeeeeeeeee', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', 50.00, 'CREDIT', '2026-01-15T11:05:01Z')
ON CONFLICT DO NOTHING;
