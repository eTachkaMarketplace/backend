ALTER TABLE advertisements
    ADD car_id BIGINT NOT NULL,
    ADD CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id);