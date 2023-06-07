ALTER TABLE aki_user
    DROP COLUMN user_type;

ALTER TABLE aki_user
    ADD COLUMN specializations JSONB NOT NULL DEFAULT '[]'::jsonb;
