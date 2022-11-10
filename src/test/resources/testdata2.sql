
INSERT INTO FILMS (name, release_date, description, duration, mpa, last_update)
VALUES ('film1', '1989-08-08', 'description of the film', 139, 1, '2022-09-09');
INSERT INTO FILMS (name, release_date, description, duration, mpa, last_update)
VALUES ('film2', '1949-04-17', 'description of the film2', 170, 3, '2022-09-09');

INSERT INTO LIKED_FILMS (film_id, user_id, last_update) VALUES (1, 1, '2022-09-09');
INSERT INTO LIKED_FILMS (film_id, user_id, last_update) VALUES (1, 2, '2022-09-09');
INSERT INTO LIKED_FILMS (film_id, user_id, last_update) VALUES (2, 1, '2022-09-09');