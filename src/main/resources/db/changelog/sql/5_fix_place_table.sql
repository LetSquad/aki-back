ALTER TABLE place
    ALTER COLUMN place_type DROP NOT NULL;

ALTER TABLE place
    DROP COLUMN specialization;

ALTER TABLE place
    ADD COLUMN specialization JSONB NOT NULL;
