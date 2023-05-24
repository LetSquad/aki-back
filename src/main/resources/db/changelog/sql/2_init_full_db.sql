DROP TABLE aki_user CASCADE;

DROP TABLE user_refresh_token CASCADE;

DROP TABLE place CASCADE;

DROP TABLE place_review CASCADE;

CREATE TABLE aki_user
(
    id           BIGSERIAL PRIMARY KEY,
    email        TEXT NOT NULL UNIQUE,
    password     TEXT NOT NULL,
    role         TEXT NOT NULL,
    type         TEXT,
    first_name   TEXT NOT NULL,
    last_name    TEXT NOT NULL,
    middle_name  TEXT,
    phone        TEXT NOT NULL UNIQUE,
    user_image   TEXT,
    inn          TEXT,
    organization TEXT,
    logo_image   TEXT,
    job_title    TEXT,
    is_activated BOOLEAN,
    is_banned    BOOLEAN,
    ban_reason   TEXT,
    admin_id     BIGINT REFERENCES aki_user (id)
);

CREATE TABLE coordinates
(
    id        BIGSERIAL PRIMARY KEY,
    longitude FLOAT,
    latitude  FLOAT
);

CREATE TABLE user_refresh_token
(
    user_email    TEXT PRIMARY KEY REFERENCES aki_user (email),
    refresh_token TEXT
);

CREATE TABLE area
(
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT NOT NULL REFERENCES aki_user (id),
    name           TEXT   NOT NULL,
    description    TEXT   NOT NULL,
    area_image     TEXT,
    status         TEXT   NOT NULL,
    address        TEXT   NOT NULL,
    website        TEXT,
    email          TEXT   NOT NULL,
    phone          TEXT   NOT NULL,
    coordinates_id BIGINT REFERENCES coordinates (id),
    ban_reason     TEXT,
    admin_id       BIGINT REFERENCES aki_user (id)
);

CREATE TABLE place
(
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT NOT NULL REFERENCES aki_user (id),
    area_id        BIGINT NOT NULL REFERENCES area (id),
    type           TEXT   NOT NULL,
    name           TEXT   NOT NULL,
    specialization TEXT   NOT NULL,
    description    TEXT   NOT NULL,
    address        TEXT   NOT NULL,
    coordinates_id BIGINT REFERENCES coordinates (id),
    phone          TEXT   NOT NULL,
    email          TEXT   NOT NULL,
    website        TEXT,
    level_number   INT,
    services       JSONB, --jsonb
    rules          JSONB, --jsonb,
    accessibility  JSONB, --jsonb,
    full_area      INT    NOT NULL,
    rentable_area  INT    NOT NULL,
    facilities     JSONB, --jsonb,
    equipments     JSONB, --jsonb,
    capacity_min   INT    NOT NULL,
    capacity_max   INT    NOT NULL,
    status         TEXT   NOT NULL,
    ban_reason     TEXT,
    admin_id       BIGINT REFERENCES aki_user (id)
);

CREATE TABLE favorite_place
(
    id       BIGSERIAL PRIMARY KEY,
    place_id BIGINT NOT NULL REFERENCES place (id),
    user_id  BIGINT NOT NULL REFERENCES aki_user (id)
);

CREATE TABLE place_image
(
    id       BIGSERIAL PRIMARY KEY,
    place_id BIGINT REFERENCES place (id),
    image    TEXT NOT NULL,
    priority INT  NOT NULL
);

CREATE TABLE rent
(
    id         BIGSERIAL PRIMARY KEY,
    place_id   BIGINT NOT NULL REFERENCES place (id),
    user_id    BIGINT NOT NULL REFERENCES aki_user (id),
    status     TEXT   NOT NULL,
    ban_reason TEXT,
    admin_id   BIGINT REFERENCES aki_user (id)
);

CREATE TABLE rent_slot
(
    id         BIGSERIAL PRIMARY KEY,
    place_id   BIGINT    NOT NULL REFERENCES place (id),
    time_start TIMESTAMP NOT NULL,
    time_end   TIMESTAMP NOT NULL,
    status     TEXT      NOT NULL,
    price      DECIMAL   NOT NULL
);

CREATE TABLE rent_slot__rent
(
    id           BIGSERIAL PRIMARY KEY,
    rent_id      BIGINT NOT NULL REFERENCES rent (id),
    rent_slot_id BIGINT NOT NULL REFERENCES rent_slot (id)
);

CREATE TABLE place_review
(
    id          BIGSERIAL PRIMARY KEY,
    rent_id     BIGINT NOT NULL REFERENCES rent (id),
    rating      DECIMAL(2, 1)
        CONSTRAINT chk_Ratings CHECK (rating >= 0 AND rating <= 5),
    review_text TEXT   NOT NULL,
    status      TEXT   NOT NULL,
    ban_reason  TEXT,
    admin_id    BIGINT REFERENCES aki_user (id)
);
