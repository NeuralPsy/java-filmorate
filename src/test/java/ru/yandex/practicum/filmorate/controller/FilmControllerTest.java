package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.DescriptionValidationException;
import ru.yandex.practicum.filmorate.exception.FilmDurationValidationException;
import ru.yandex.practicum.filmorate.exception.FilmNameValidationException;
import ru.yandex.practicum.filmorate.exception.ReleaseDateValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private  FilmController filmController = new FilmController();

    private Film film;

    @BeforeEach
    void createFilm(){
        film = new Film();
    }


    @Test
    void createFilmWithoutName(){
        film.setDuration(123456789);
        film.setReleaseDate("2010-04-20");
        film.setName("");
        film.setDescription("This is a movie about little little dog that had no arms. " +
                "Arms had no tooth and nobody knew what to do with this king od animal. ");
        FilmNameValidationException exception = Assertions.assertThrows(FilmNameValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("Film name cannot be empty", exception.getMessage());
    }

    @Test
    void createFilmWithWrongReleaseDate(){
        film.setDuration(123456789);
        film.setReleaseDate("1894-12-28");
        film.setName("Spawn");
        film.setDescription("This is a movie about little little dog that had no arms. " +
                "Arms had no tooth and nobody knew what to do with this king od animal. ");
        ReleaseDateValidationException exception = Assertions.assertThrows(ReleaseDateValidationException.class,
                () -> filmController.create(film));
        String msg = "Film release date should not be earlier than December 28th of 1895";
        Assertions.assertEquals(msg, exception.getMessage());
    }

    @Test
    void createFilmWithNegativeDurationValue(){
        film.setDuration(-1);
        film.setReleaseDate("2020-12-28");
        film.setName("Spawn");
        film.setDescription("This is a movie about little little dog that had no arms. " +
                "Arms had no tooth and nobody knew what to do with this king od animal. ");
        FilmDurationValidationException exception = Assertions.assertThrows(FilmDurationValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("Duration value should be positive", exception.getMessage());
    }

    @Test
    void createFilmWithLondDescription(){
        film.setDuration(-1);
        film.setReleaseDate("2020-12-28");
        film.setName("Spawn");
        film.setDescription("This is a movie about little little dog that had no arms. " +
                "Arms had no tooth and nobody knew what to do with this king od animal. By the way, who has no bad " +
                "habits and wants to try shower with transparent sticky horse that likes orange clothes?");
        DescriptionValidationException exception = Assertions.assertThrows(DescriptionValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("Film description should not be more than 200 symbols",
                exception.getMessage());
    }




}