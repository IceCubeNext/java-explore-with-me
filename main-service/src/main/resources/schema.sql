DROP TABLE IF EXISTS request;
DROP TABLE IF EXISTS event_compilation;
DROP TABLE IF EXISTS compilation;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS location;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name  VARCHAR(250)                        NOT NULL,
    email VARCHAR(254)                        NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS location
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    lat FLOAT                               NOT NULL,
    lon FLOAT                               NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR(50)                         NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    title  VARCHAR(250)                        NOT NULL,
    pinned BOOLEAN                             NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    title             VARCHAR(120)                        NOT NULL,
    annotation        VARCHAR(2000)                       NOT NULL,
    description       VARCHAR(7000)                       NOT NULL,
    event_date        TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    created_on        TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    paid              BOOLEAN                             NOT NULL,
    views             BIGINT                              NOT NULL,
    participant_limit BIGINT                              NOT NULL,
    state             VARCHAR(10)                         NOT NULL,
    location          BIGINT                              NOT NULL,
    initiator         BIGINT                              NOT NULL,
    category          BIGINT                              NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_event_location
        FOREIGN KEY (location)
            REFERENCES location (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_event_user
        FOREIGN KEY (initiator)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_event_category
        FOREIGN KEY (category)
            REFERENCES category (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_compilation
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    CONSTRAINT event_compilation PRIMARY KEY (compilation_id, event_id),
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
    id_user  BIGINT                      NOT NULL,
    id_event BIGINT                      NOT NULL,
    created  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status   VARCHAR(10)                 NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id_user, id_event),
    CONSTRAINT fk_request_user
        FOREIGN KEY (id_user)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_request_event
        FOREIGN KEY (id_event)
            REFERENCES event (id)
            ON DELETE CASCADE
);