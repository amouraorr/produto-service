CREATE TABLE produtos (
                          id SERIAL PRIMARY KEY,
                          nome VARCHAR(255) NOT NULL,
                          sku VARCHAR(255) NOT NULL UNIQUE,
                          preco NUMERIC(10, 2) NOT NULL
);
