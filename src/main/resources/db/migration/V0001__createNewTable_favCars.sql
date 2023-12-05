CREATE TABLE users_advertisements
(
    user_id BIGINT NOT NULL
        REFERENCES users (id),
    advertisements_id INT    NOT NULL
        REFERENCES cars (id),
    PRIMARY KEY (user_id, advertisements_id)
);