ALTER TABLE advertisements
    ADD preview_image_id BIGINT REFERENCES images (id);

ALTER TABLE cars
    ADD CONSTRAINT advertisement_id_fk FOREIGN KEY (advertisement_id) REFERENCES advertisements (id),
    ADD CONSTRAINT body_type_id_fk FOREIGN KEY (body_type_id) REFERENCES body_types (id),
    ADD CONSTRAINT car_mark_id_fk FOREIGN KEY (car_mark_id) REFERENCES cars_marks (id),
    ADD CONSTRAINT color_id_fk FOREIGN KEY (color_id) REFERENCES colors (id),
    ADD CONSTRAINT drive_type_id_fk FOREIGN KEY (drive_type_id) REFERENCES drive_types (id),
    ADD CONSTRAINT engine_id_fk FOREIGN KEY (engine_id) REFERENCES engines (id),
    ADD CONSTRAINT technical_state_id_fk FOREIGN KEY (technical_state_id) REFERENCES technical_states (id),
    ADD CONSTRAINT transmission_id_fk FOREIGN KEY (transmission_id) REFERENCES transmissions (id);

ALTER TABLE body_types
    ADD CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id);

ALTER TABLE cars_marks
    ADD CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id);

ALTER TABLE colors
    ADD CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id);

ALTER TABLE drive_types
    ADD CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id);

ALTER TABLE engines
    ADD CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id);

ALTER TABLE technical_states
    ADD CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id);

ALTER TABLE transmissions
    ADD CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id);