DROP TABLE IF EXISTS stat;

CREATE TABLE IF NOT EXISTS stat
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    app      VARCHAR(64)                         NOT NULL,
    uri      VARCHAR(256)                        NOT NULL,
    ip       VARCHAR(64)                         NOT NULL,
    hit_time TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    CONSTRAINT pk_stat PRIMARY KEY (id)
);