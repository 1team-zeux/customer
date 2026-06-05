CREATE TABLE IF NOT EXISTS products (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    price      NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS orders (
    id          BIGSERIAL PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL,
    status      VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    total_price NUMERIC(10,2),
    created_at  TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS payments (
    id         BIGSERIAL PRIMARY KEY,
    order_id   BIGINT NOT NULL,
    amount     NUMERIC(10,2) NOT NULL,
    status     VARCHAR(30) NOT NULL DEFAULT 'PROCESSING',
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS inventory (
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE,
    quantity   INT NOT NULL DEFAULT 100,
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS recommendations (
    id          BIGSERIAL PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    product_ids JSONB,
    created_at  TIMESTAMP DEFAULT NOW()
);

INSERT INTO products (name, price) VALUES
    ('Widget A', 9.99),
    ('Widget B', 19.99),
    ('Widget C', 4.99)
ON CONFLICT DO NOTHING;

INSERT INTO inventory (product_id, quantity) VALUES (1, 100), (2, 50), (3, 200)
ON CONFLICT DO NOTHING;
