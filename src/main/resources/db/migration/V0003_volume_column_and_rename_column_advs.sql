ALTER TABLE engines
    ADD volume varchar(30);

ALTER TABLE advertisements
    RENAME COLUMN name TO ownerName;