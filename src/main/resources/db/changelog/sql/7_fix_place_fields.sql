ALTER TABLE place
    ALTER COLUMN capacity_min DROP NOT NULL;

ALTER TABLE place
    ALTER COLUMN capacity_max DROP NOT NULL;

ALTER TABLE place
    ADD parking BOOLEAN NULL;
