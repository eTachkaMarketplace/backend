CREATE TABLE IF NOT EXISTS cities
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email       varchar(255) UNIQUE,
    enabled     BOOLEAN,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    password    VARCHAR(255),
    phone       VARCHAR(255),
    photo       VARCHAR(255),
    unique_code VARCHAR(255),
    unique (email)
);

ALTER TABLE users
    ADD CONSTRAINT unique_email_constraint UNIQUE (email);

CREATE TABLE IF NOT EXISTS authorities
(
    user_id   BIGINT NOT NULL,
    authority VARCHAR(255) CHECK (authority IN ('USER', 'ADMIN')),
    FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE IF NOT EXISTS regions
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(255),
    city_id BIGINT UNIQUE,

    CONSTRAINT city_id_fk FOREIGN KEY (city_id) REFERENCES cities (id)
);

CREATE TABLE IF NOT EXISTS technical_states
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS transmissions
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS cars_models
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS cars_marks
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(255),
    car_model_id BIGINT UNIQUE,

    CONSTRAINT car_model_id_fk FOREIGN KEY (car_model_id) REFERENCES cars_models (id)
);

CREATE TABLE IF NOT EXISTS drive_types
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS colors
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS body_types
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS engines
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS cars
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    car_number         VARCHAR(255),
    mileage            INTEGER,
    vin_number         VARCHAR(255),
    year_to_create     VARCHAR(255),
    body_type_id       BIGINT,
    car_mark_id        BIGINT,
    color_id           BIGINT,
    drive_type_id      BIGINT,
    engine_id          BIGINT,
    technical_state_id BIGINT,
    transmission_id    BIGINT,

    CONSTRAINT body_type_id_fk FOREIGN KEY (body_type_id) REFERENCES body_types (id),
    CONSTRAINT car_mark_id_fk FOREIGN KEY (car_mark_id) REFERENCES cars_marks (id),
    CONSTRAINT color_id_fk FOREIGN KEY (color_id) REFERENCES colors (id),
    CONSTRAINT drive_type_id_fk FOREIGN KEY (drive_type_id) REFERENCES drive_types (id),
    CONSTRAINT engine_id_fk FOREIGN KEY (engine_id) REFERENCES engines (id),
    CONSTRAINT technical_state_id_fk FOREIGN KEY (technical_state_id) REFERENCES technical_states (id),
    CONSTRAINT transmission_id_fk FOREIGN KEY (transmission_id) REFERENCES transmissions (id)
);

CREATE TABLE IF NOT EXISTS advertisements
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(255),
    name        VARCHAR(255),
    price       INTEGER,
    car_id      BIGINT UNIQUE,
    region_id   BIGINT,
    user_id     BIGINT,

    CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id),
    CONSTRAINT region_id_fk FOREIGN KEY (region_id) REFERENCES regions (id),
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS images
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    content_type     VARCHAR(255),
    is_preview_image BOOLEAN,
    name             VARCHAR(255),
    resource         OID,
    size             BIGINT,
    advertisement_id BIGINT,

    CONSTRAINT advertisement_id_fk FOREIGN KEY (advertisement_id) REFERENCES advertisements (id)
);

CREATE TABLE IF NOT EXISTS users_favorite_cars
(
    user_id          BIGINT NOT NULL,
    advertisement_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, advertisement_id),
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT advertisement_id_fk FOREIGN KEY (advertisement_id) REFERENCES advertisements (id)
);