ALTER TABLE images
    ALTER COLUMN resource TYPE bytea USING resource::bytea;
