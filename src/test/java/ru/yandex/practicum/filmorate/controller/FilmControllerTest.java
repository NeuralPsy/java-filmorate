package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.film.DescriptionValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmDurationValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNameValidationException;
import ru.yandex.practicum.filmorate.exception.film.ReleaseDateValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FIlmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {

    @Autowired
    private  FilmController filmController;

    private Film film;

    @BeforeEach
    void createFilm(){
        film = Film.builder().build();
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
        Film film1 = Film.builder().duration(123456789).releaseDate("1894-12-28").name("Spawn")
                .description("This is a movie about little little dog that had no arms. " +
                        "Arms had no tooth and nobody knew what to do with this king od animal. ").build();

        film.setDuration(123456789);
        film.setReleaseDate("1894-12-28");
        film.setName("Spawn");
        film.setDescription("This is a movie about little little dog that had no arms. " +
                "Arms had no tooth and nobody knew what to do with this king od animal. ");
        ReleaseDateValidationException exception = Assertions.assertThrows(ReleaseDateValidationException.class,
                () -> filmController.create(film1));
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
    void createFilmWithLongDescription(){
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