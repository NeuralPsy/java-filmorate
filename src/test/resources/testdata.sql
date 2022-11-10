DELETE FROM USERS;
DELETE FROM FILMS;

INSERT INTO USERS (name, login, email, birthday) VALUES ( 'user1', 'user1_login', 'user1@mail.ru', '1990-03-15');
INSERT INTO USERS (name, login, email, birthday) VALUES ( 'user2', 'user2_login', 'user2@mail.ru', '1991-02-19');
INSERT INTO USERS (name, login, email, birthday) VALUES ( 'friend1', 'friend1_login', 'friend1@gmail.com', '1992-06-06');
INSERT INTO USERS (name, login, email, birthday) VALUES ( 'friend2', 'friend2_login', 'friend2@gmail.com', '1991-09-21');

INSERT INTO FRIENDLIST (user_id, friend_id, last_update) VALUES (2, 4, '2022-09-22');
INSERT INTO FRIENDLIST (user_id, friend_id, last_update) VALUES (2, 1, '2022-09-22');
INSERT INTO FRIENDLIST (user_id, friend_id, last_update) VALUES (3, 1, '2022-09-21');
INSERT INTO FRIENDLIST (user_id, friend_id, last_update) VALUES (3, 2, '2022-09-21');

INSERT INTO FILMS (name, release_date, description, duration, mpa, last_update)
VALUES ('film1', '1989-08-08', 'description of the film', 139, 1, '2022-09-09');
INSERT INTO FILMS (name, release_date, description, duration, mpa, last_update)
VALUES ('film2', '1949-04-17', 'description of the film2', 170, 3, '2022-09-09');

INSERT INTO LIKED_FILMS (film_id, user_id, last_update) VALUES (1, 1, '2022-09-09');
INSERT INTO LIKED_FILMS (film_id, user_id, last_update) VALUES (1, 2, '2022-09-09');
INSERT INTO LIKED_FILMS (film_id, user_id, last_update) VALUES (2, 1, '2022-09-09');