CREATE TABLE IF NOT EXISTS users_advertisements
(
    user_id BIGINT NOT NULL,
    advertisements_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, advertisements_id),

    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT advertisements_id_fk FOREIGN KEY (advertisements_id) REFERENCES advertisements (id)
);

CREATE TABLE IF NOT EXISTS advertisements
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (100) NOT NULL,
    description VARCHAR (1024),
    price INTEGER NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS cars
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    vin_number VARCHAR (80),
    year_to_create VARCHAR (20) NOT NULL,
    car_number VARCHAR (15),
    mileage INTEGER NOT NULL,
    advertisement_id BIGINT,
    body_type_id BIGINT,
    car_mark_id BIGINT,
    color_id BIGINT,
    drive_type_id BIGINT,
    engine_id BIGINT,
    technical_state_id BIGINT,
    transmission_id BIGINT
);

CREATE TABLE IF NOT EXISTS cars_marks
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (30),
    car_id BIGINT
);

CREATE TABLE IF NOT EXISTS cars_models
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (30),
    car_mark_id BIGINT,

    CONSTRAINT car_mark_id_fk FOREIGN KEY (car_mark_id) REFERENCES cars_marks (id)
);

CREATE TABLE IF NOT EXISTS regions
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (255),
    advertisement_id BIGINT,

    CONSTRAINT advertisements_id_fk FOREIGN KEY (advertisement_id) REFERENCES advertisements (id)
);

CREATE TABLE IF NOT EXISTS cities
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (80),
    region_id BIGINT,

    CONSTRAINT region_id_fk FOREIGN KEY (region_id) REFERENCES regions (id)
);

CREATE TABLE IF NOT EXISTS engines
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (50),
    car_id BIGINT
);

CREATE TABLE IF NOT EXISTS technical_states
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (50),
    car_id BIGINT
);

CREATE TABLE IF NOT EXISTS body_types
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (50),
    car_id BIGINT
);

CREATE TABLE IF NOT EXISTS drive_types
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (50),
    car_id BIGINT
);

CREATE TABLE IF NOT EXISTS colors
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (50),
    car_id BIGINT
);

CREATE TABLE IF NOT EXISTS transmissions
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (50),
    car_id BIGINT
);

CREATE TABLE IF NOT EXISTS images
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (255),
    size BIGINT,
    content_type VARCHAR (100),
    is_preview_image BOOLEAN,
    resource OID,
    advertisement_id BIGINT,

    CONSTRAINT advertisements_id_fk FOREIGN KEY (advertisement_id) REFERENCES advertisements (id)
);