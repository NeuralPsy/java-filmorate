CREATE TABLE IF NOT EXISTS map_rating  (
    rating_id integer PRIMARY KEY,
    name varchar(30),
    last_update date
    );

CREATE TABLE IF NOT EXISTS films (
    id integer PRIMARY KEY,
    name varchar(35),
    release_date date,
    description varchar(200),
    duration integer,
    mpa_rating integer REFERENCES map_rating  (rating_id),
    last_update date
    );

CREATE TABLE IF NOT EXISTS users (
    id integer PRIMARY KEY,
    email varchar,
    login varchar(30),
    name varchar(100),
    birthday date,
    last_update date
    );

CREATE TABLE IF NOT EXISTS genre (
    genre_id integer,
    name varchar(30),
    last_update date,
    CONSTRAINT genre_ids PRIMARY KEY (genre_id)
    );

CREATE TABLE IF NOT EXISTS film_genre (
    film_id integer REFERENCES films (id),
    genre_id integer REFERENCES genre (genre_id),
    last_update DATE
    );


CREATE TABLE IF NOT EXISTS liked_films  (
    film_id integer REFERENCES films (id),
    user_id integer REFERENCES users (id),
    last_update date
    );

CREATE TABLE IF NOT EXISTS friendship_status  (
    status_id integer PRIMARY KEY,
    name varchar(30),
    last_update date
    );

CREATE TABLE IF NOT EXISTS friendlist  (
    user_id integer REFERENCES users (id),
    friend_id integer REFERENCES users (id),
    status integer REFERENCES friendship_status (status_id),
    last_update date
    );