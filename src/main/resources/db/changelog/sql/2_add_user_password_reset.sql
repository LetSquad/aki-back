CREATE TABLE user_password_reset_token
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES aki_user (id),
    reset_token TEXT      NOT NULL UNIQUE,
    expire      TIMESTAMP NOT NULL
);

ALTER TABLE aki_user
    ADD activation_code TEXT NULL;


