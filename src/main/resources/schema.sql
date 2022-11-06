


CREATE TABLE IF NOT EXISTS MPA (
    RATING_ID SMALLINT,
    NAME CHARACTER VARYING(50),
    LAST_UPDATE DATE,
    CONSTRAINT MPA_PK PRIMARY KEY (RATING_ID)
);

CREATE TABLE IF NOT EXISTS FILMS (
      ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
      NAME CHARACTER VARYING(50),
      RELEASE_DATE DATE,
      DESCRIPTION CHARACTER VARYING(200),
      DURATION BIGINT,
      MPA_RATING SMALLINT REFERENCES MPA(RATING_ID),
      LAST_UPDATE DATE,
      CONSTRAINT FILMS_PK PRIMARY KEY (ID)
);

CREATE INDEX IF NOT EXISTS FILMS_ID_IDX ON FILMS (ID);

CREATE TABLE IF NOT EXISTS USERS (
      ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
      EMAIL CHARACTER VARYING,
      LOGIN CHARACTER VARYING NOT NULL,
      NAME CHARACTER VARYING,
      BIRTHDAY DATE,
      LAST_UPDATE DATE,
      CONSTRAINT USERS_PK PRIMARY KEY (ID)
);

CREATE INDEX IF NOT EXISTS USERS_ID_IDX ON USERS (ID);

CREATE TABLE IF NOT EXISTS LIKED_FILMS (
        FILM_ID BIGINT NOT NULL,
        USER_ID BIGINT NOT NULL,
        LAST_UPDATE DATE,
        CONSTRAINT LIKED_FILMS_FK FOREIGN KEY (FILM_ID) REFERENCES FILMS(ID),
        CONSTRAINT LIKED_FILMS_FK_1 FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);



CREATE TABLE IF NOT EXISTS GENRE (
      GENRE_ID SMALLINT,
      NAME CHARACTER VARYING,
      LAST_UPDATE DATE,
      CONSTRAINT GENRE_PK PRIMARY KEY (GENRE_ID)
);

CREATE INDEX IF NOT EXISTS GENRE_ID_IDX ON GENRE (GENRE_ID);

CREATE TABLE IF NOT EXISTS FILM_GENRE (
       FILM_ID BIGINT REFERENCES FILMS(ID),
       GENRE_ID SMALLINT REFERENCES GENRE(GENRE_ID),
       LAST_UPDATE DATE
);


CREATE TABLE IF NOT EXISTS FRIENDSHIP_STATUS (
          STATUS_ID BOOLEAN NOT NULL,
          NAME CHARACTER VARYING(30),
          LAST_UPDATE DATE,
          CONSTRAINT FRIENDSHIP_STATUS_PK PRIMARY KEY (STATUS_ID)
);

CREATE INDEX IF NOT EXISTS FSST_ID_IDX ON FRIENDSHIP_STATUS (STATUS_ID);


CREATE TABLE IF NOT EXISTS FRIENDLIST (
           USER_ID BIGINT REFERENCES USERS(ID),
           FRIEND_ID BIGINT REFERENCES USERS(ID),
           STATUS BOOLEAN REFERENCES FRIENDSHIP_STATUS(STATUS_ID),
           LAST_UPDATE DATE NOT NULL
);



