DROP TABLE IF EXISTS request;
DROP TABLE IF EXISTS event_compilation;
DROP TABLE IF EXISTS compilation;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name  VARCHAR(250)                        NOT NULL,
    email VARCHAR(254)                        NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR(50) UNIQUE                  NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    title  VARCHAR(250)                        NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    title              VARCHAR(120)                        NOT NULL,
    annotation         VARCHAR(2000)                       NOT NULL,
    description        VARCHAR(7000)                       NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    paid               BOOLEAN,
    views              BIGINT,
    participant_limit  BIGINT,
    request_moderation BOOLEAN,
    state              VARCHAR(10),
    lat                FLOAT,
    lon                FLOAT,
    initiator_id       BIGINT                              NOT NULL,
    category_id        BIGINT                              NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_event_user
        FOREIGN KEY (initiator_id)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_event_category
        FOREIGN KEY (category_id)
            REFERENCES category (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_compilation
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    compilation_id BIGINT                              NOT NULL,
    event_id       BIGINT                              NOT NULL,
    CONSTRAINT pk_event_compilation PRIMARY KEY (id),
    CONSTRAINT fk_compilation
        FOREIGN KEY (compilation_id)
            REFERENCES compilation (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_event
        FOREIGN KEY (event_id)
            REFERENCES event (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS request
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    requester_id BIGINT                              NOT NULL,
    event_id     BIGINT                              NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    status       VARCHAR(10)                         NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_request_user
        FOREIGN KEY (requester_id)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_request_event
        FOREIGN KEY (event_id)
            REFERENCES event (id)
            ON DELETE CASCADE
);