DROP TABLE instrument IF EXISTS;

CREATE TABLE instrument  (
    instrument_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    type VARCHAR(20),
    created_date DATE,
    amount NUMERIC(24,2)
);

DROP TABLE instrument_price_modifier IF EXISTS;

CREATE TABLE instrument_price_modifier (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    type VARCHAR(20),
    multiplier NUMERIC(24,2)
);

INSERT INTO instrument_price_modifier (type, multiplier) VALUES ('INSTRUMENT1', 2.0);
INSERT INTO instrument_price_modifier (type, multiplier) VALUES ('INSTRUMENT3', 4.0);
INSERT INTO instrument_price_modifier (type, multiplier) VALUES ('INSTRUMENT4', 6.0);
