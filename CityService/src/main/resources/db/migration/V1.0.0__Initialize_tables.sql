CREATE SEQUENCE hibernate_sequence START 1;

CREATE TABLE cities
(
    city_id   SERIAL,
    city_name VARCHAR(100) NOT NULL,
    country   VARCHAR(100) NOT NULL,
    PRIMARY KEY (city_id)
);

CREATE TABLE images
(
    image_id SERIAL,
    name     VARCHAR(100) NOT NULL,
    image    BYTEA,
--  join column - cityId type city
--     city_id  INTEGER      NOT NULL,
    PRIMARY KEY (image_id)
);

CREATE TABLE attractions
(
    attraction_id SERIAL,
    name          VARCHAR(100),
--  join column - cityId type city
    city_id       INTEGER ,
    PRIMARY KEY (attraction_id)
);
