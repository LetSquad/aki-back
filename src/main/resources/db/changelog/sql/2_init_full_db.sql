DROP TABLE aki_user CASCADE;

DROP TABLE user_refresh_token CASCADE;

DROP TABLE place CASCADE;

DROP TABLE place_review CASCADE;

CREATE TABLE aki_user
(
    id           BIGINT PRIMARY KEY,
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
    job_title    TEXT,
    area_id      BIGINT UNIQUE,
    is_activated BOOLEAN,
    is_banned    BOOLEAN,
    ban_reason   TEXT,
    admin_id     BIGINT UNIQUE
);

CREATE TABLE user_refresh_token
(
    user_email    TEXT PRIMARY KEY,
    refresh_token TEXT
);

CREATE TABLE favorite_place
(
    id       BIGINT PRIMARY KEY,
    place_id BIGINT UNIQUE,
    user_id  BIGINT UNIQUE
);

CREATE TABLE area
(
    id             BIGINT PRIMARY KEY,
    name           TEXT NOT NULL,
    description    TEXT NOT NULL,
    area_image     TEXT,
    status         TEXT NOT NULL,
    address        TEXT NOT NULL,
    website        TEXT,
    email          TEXT NOT NULL,
    coordinates_id BIGINT UNIQUE,
    ban_reason     TEXT,
    admin_id       BIGINT UNIQUE
);

CREATE TABLE coordinates
(
    id BIGINT PRIMARY KEY,
    ln float,
    lt float
);

CREATE TABLE place
(
    id             BIGINT PRIMARY KEY,
    user_id        BIGINT UNIQUE,
    area_id        BIGINT UNIQUE,
    type           TEXT NOT NULL,
    name           TEXT NOT NULL,
    specialization TEXT NOT NULL,
    description    TEXT NOT NULL,
    address        TEXT NOT NULL,
    coordinates_id BIGINT UNIQUE,
    phone          TEXT NOT NULL,
    email          TEXT NOT NULL,
    website        TEXT,
    level_number   INT,
    services       TEXT, --jsonb
    rules          TEXT, --jsonb,
    accessibility  TEXT, --jsonb,
    full_area      INT  NOT NULL,
    rentable_area  INT  NOT NULL,
    facilities     TEXT, --jsonb,
    equipments     TEXT, --jsonb,
    capacity_min   INT  NOT NULL,
    capacity_max   INT  NOT NULL,
    status         TEXT NOT NULL,
    ban_reason     TEXT,
    admin_id       BIGINT UNIQUE
);

CREATE TABLE place_image
(
    id       BIGINT PRIMARY KEY,
    place_id BIGINT UNIQUE,
    image    TEXT NOT NULL,
    priority INT  NOT NULL
);

CREATE TABLE rent
(
    id         BIGINT PRIMARY KEY,
    place_id   BIGINT UNIQUE,
    user_id    BIGINT UNIQUE,
    status     TEXT NOT NULL,
    ban_reason TEXT,
    admin_id   BIGINT UNIQUE
);

CREATE TABLE rent_slot
(
    id         BIGINT PRIMARY KEY,
    place_id   BIGINT UNIQUE,
    time_start TIMESTAMP NOT NULL,
    time_end   TIMESTAMP NOT NULL,
    status     TEXT      NOT NULL,
    price      DECIMAL   NOT NULL
);

CREATE TABLE rent_slot__rent
(
    id           BIGINT PRIMARY KEY,
    rent_id      BIGINT UNIQUE,
    rent_slot_id BIGINT UNIQUE
);

CREATE TABLE place_review
(
    id          BIGINT PRIMARY KEY,
    rent_id     BIGINT UNIQUE,
    rating      DECIMAL(2, 1)
        CONSTRAINT chk_Ratings CHECK (rating >= 0 AND rating <= 5),
    review_text TEXT NOT NULL,
    status      TEXT NOT NULL,
    ban_reason  TEXT,
    admin_id    BIGINT UNIQUE
);


ALTER TABLE "user_refresh_token"
    ADD FOREIGN KEY ("user_email") REFERENCES "aki_user" ("email");

ALTER TABLE "rent"
    ADD FOREIGN KEY ("place_id") REFERENCES "place" ("id");

ALTER TABLE "place"
    ADD FOREIGN KEY ("area_id") REFERENCES "area" ("id");

ALTER TABLE "aki_user"
    ADD FOREIGN KEY ("area_id") REFERENCES "area" ("id");

ALTER TABLE "place_image"
    ADD FOREIGN KEY ("place_id") REFERENCES "place" ("id");

ALTER TABLE "place"
    ADD FOREIGN KEY ("user_id") REFERENCES "aki_user" ("id");

ALTER TABLE "aki_user"
    ADD FOREIGN KEY ("id") REFERENCES "place" ("admin_id");

ALTER TABLE "aki_user"
    ADD FOREIGN KEY ("id") REFERENCES "aki_user" ("admin_id");

ALTER TABLE "aki_user"
    ADD FOREIGN KEY ("id") REFERENCES "area" ("admin_id");

ALTER TABLE "aki_user"
    ADD FOREIGN KEY ("id") REFERENCES "place_review" ("admin_id");

ALTER TABLE "aki_user"
    ADD FOREIGN KEY ("id") REFERENCES "rent" ("admin_id");

ALTER TABLE "rent_slot"
    ADD FOREIGN KEY ("place_id") REFERENCES "place" ("id");

ALTER TABLE "rent"
    ADD FOREIGN KEY ("id") REFERENCES "place_review" ("rent_id");

ALTER TABLE "rent_slot__rent"
    ADD FOREIGN KEY ("rent_id") REFERENCES "rent" ("id");

ALTER TABLE "rent_slot__rent"
    ADD FOREIGN KEY ("rent_slot_id") REFERENCES "rent_slot" ("id");

ALTER TABLE "place"
    ADD FOREIGN KEY ("coordinates_id") REFERENCES "coordinates" ("id");

ALTER TABLE "area"
    ADD FOREIGN KEY ("coordinates_id") REFERENCES "coordinates" ("id");

ALTER TABLE "place"
    ADD FOREIGN KEY ("id") REFERENCES "favorite_place" ("place_id");

ALTER TABLE "aki_user"
    ADD FOREIGN KEY ("id") REFERENCES "favorite_place" ("user_id");
