CREATE TABLE aki_user
(
    id           BIGSERIAL PRIMARY KEY,
    email        TEXT NOT NULL UNIQUE,
    password     TEXT NOT NULL,
    role         TEXT NOT NULL,
    first_name   TEXT NOT NULL,
    last_name    TEXT NOT NULL,
    middle_name  TEXT,
    phone        TEXT NOT NULL UNIQUE,
    user_image   TEXT,
    inn          TEXT,
    organization TEXT,
    job_title    TEXT,
    is_active    BOOLEAN NOT NULL,
    is_banned    BOOLEAN NOT NULL
);

CREATE TABLE user_refresh_token
(
    user_id BIGINT PRIMARY KEY REFERENCES aki_user(id),
    refresh_token TEXT
);

CREATE TABLE place
(
    id                     BIGINT PRIMARY KEY,
    name                   TEXT NOT NULL,
    specialization         TEXT NOT NULL,
    description            TEXT NOT NULL,
    address                TEXT NOT NULL,
    work_hours             TEXT NOT NULL,
    phone                  TEXT NOT NULL,
    email                  TEXT NOT NULL,
    site                   TEXT NOT NULL,
    services               TEXT NOT NULL,
    area                   TEXT NOT NULL,
    free_rentable_area     TEXT NOT NULL,
    collective_use_objects TEXT NOT NULL,
    special_equip          TEXT NOT NULL,
    landlords_count        TEXT NOT NULL,
    image                  TEXT NOT NULL
);

CREATE TABLE place_review
(
    id          BIGINT PRIMARY KEY,
    place_id    BIGINT REFERENCES place (id),
    rating      decimal(2, 1)
        CONSTRAINT chk_Ratings CHECK (rating >= 0 AND rating <= 5),
    review_text TEXT
);
