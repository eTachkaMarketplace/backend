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
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (100) NOT NULL,
    description VARCHAR (1024),
    price INTEGER NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS cars
(
    id BIGINT NOT NULL PRIMARY KEY,
    vin_number VARCHAR (80),
    year_to_create VARCHAR (20) NOT NULL,
    car_number VARCHAR (15),
    mileage INTEGER NOT NULL,
    advertisement_id BIGINT NOT NULL,

    CONSTRAINT advertisements_id_fk FOREIGN KEY (advertisement_id) REFERENCES advertisements (id)
);

CREATE TABLE IF NOT EXISTS cars_marks
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (30) NOT NULL,
    car_id BIGINT,

    CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id)
);

CREATE TABLE IF NOT EXISTS cars_models
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (30) NOT NULL,
    car_mark_id BIGINT NOT NULL,

    CONSTRAINT car_mark_id_fk FOREIGN KEY (car_mark_id) REFERENCES cars_marks (id)
);

CREATE TABLE IF NOT EXISTS regions
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (255) NOT NULL,
    advertisements_id BIGINT,

    CONSTRAINT advertisements_id_fk FOREIGN KEY (advertisements_id) REFERENCES advertisements (id)
);

CREATE TABLE IF NOT EXISTS cities
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (80) NOT NULL,
    region_id BIGINT NOT NULL,

    CONSTRAINT region_id_fk FOREIGN KEY (region_id) REFERENCES regions (id)
);

CREATE TABLE IF NOT EXISTS engines
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (50) NOT NULL,
    car_id BIGINT,

    CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id)
);

CREATE TABLE IF NOT EXISTS technical_states
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (50) NOT NULL,
    car_id BIGINT,

    CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id)
);

CREATE TABLE IF NOT EXISTS body_types
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (50) NOT NULL,
    car_id BIGINT,

    CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id)
);

CREATE TABLE IF NOT EXISTS drive_types
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (50) NOT NULL,
    car_id BIGINT,

    CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id)
);

CREATE TABLE IF NOT EXISTS colors
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (50) NOT NULL,
    car_id BIGINT,

    CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id)
);

CREATE TABLE IF NOT EXISTS transmissions
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (50) NOT NULL,
    car_id BIGINT,

    CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id)
);

CREATE TABLE IF NOT EXISTS images
(
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR (255),
    size BIGINT,
    content_type VARCHAR (100),
    is_preview_image BOOLEAN,
    resource OID,
    advertisement_id BIGINT,

    CONSTRAINT advertisements_id_fk FOREIGN KEY (advertisement_id) REFERENCES advertisements (id)
);