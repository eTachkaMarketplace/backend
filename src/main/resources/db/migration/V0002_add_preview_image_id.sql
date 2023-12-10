ALTER TABLE advertisements
    ADD preview_image_id BIGINT REFERENCES images (id);