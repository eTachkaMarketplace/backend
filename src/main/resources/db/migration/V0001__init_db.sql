-- Image
CREATE TABLE images
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name              VARCHAR(100)             NOT NULL,
    content           BYTEA                    NOT NULL,
    content_type      VARCHAR(100)             NOT NULL,
    size              BIGINT                   NOT NULL,
    created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);

-- User
CREATE TABLE users
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name        VARCHAR(40)              NOT NULL,
    last_name         VARCHAR(40),
    email             VARCHAR(254)             NOT NULL UNIQUE,
    password          VARCHAR(300)             NOT NULL,
    phone             VARCHAR(20),
    photo             BIGINT REFERENCES images (id),
    enabled           BOOLEAN                  NOT NULL,
    unique_code       VARCHAR(20) UNIQUE,
    authorities       VARCHAR(20)[]            NOT NULL DEFAULT '{}'::VARCHAR(20)[],
    created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);


-- Advertisement
CREATE TABLE cars
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    brand             VARCHAR(50)    NOT NULL,
    model             VARCHAR(50)    NOT NULL,
    vin               VARCHAR(20)    NOT NULL,
    year              INTEGER        NOT NULL,
    price             DECIMAL(10, 2) NOT NULL,
    license_plate     VARCHAR(10)    NOT NULL,
    mileage           INTEGER        NOT NULL,
    transmission_type VARCHAR(50)    NOT NULL,
    engine_type       VARCHAR(50)    NOT NULL,
    engine_volume     FLOAT(3)       NOT NULL,
    technical_state   VARCHAR(50)    NOT NULL,
    body_type         VARCHAR(50)    NOT NULL,
    drive_type        VARCHAR(50)    NOT NULL,
    color             VARCHAR(50)    NOT NULL
);

CREATE TABLE advertisements
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description       VARCHAR(1000)            NOT NULL,
    region            VARCHAR(50)              NOT NULL,
    category          VARCHAR(50)              NOT NULL,
    car_id            BIGINT REFERENCES cars (id),
    user_id           BIGINT REFERENCES users (id),
    contact_name      VARCHAR(40)              NOT NULL,
    contact_phone     VARCHAR(20)              NOT NULL,
    preview_image_id  BIGINT REFERENCES images (id),
    is_active         BOOLEAN                  NOT NULL,
    created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE advertisement_images
(
    advertisement_id BIGINT REFERENCES advertisements (id),
    image_id         BIGINT REFERENCES images (id),
    PRIMARY KEY (advertisement_id, image_id)
);

CREATE TABLE user_favorite_advertisements
(
    user_id          BIGINT REFERENCES users (id),
    advertisement_id BIGINT REFERENCES advertisements (id),
    PRIMARY KEY (user_id, advertisement_id)
);
