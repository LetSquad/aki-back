CREATE TABLE "user"
(
    id          BIGINT PRIMARY KEY,
    first_name  TEXT NOT NULL,
    last_name   TEXT NOT NULL,
    middle_name TEXT,
    email       TEXT NOT NULL,
    password    TEXT NOT NULL,
    phone       JSONB,
    inn         TEXT,
    job_title   TEXT,
    image       TEXT,
    role        TEXT
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