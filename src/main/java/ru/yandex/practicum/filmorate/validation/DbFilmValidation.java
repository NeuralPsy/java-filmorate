package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.exception.user.UserIdentificationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component("dbFilmValidation")
@Slf4j
public class DbFilmValidation implements FilmValidation{

    private final JdbcTemplate jdbcTemplate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DbFilmValidation(JdbcTemplate jdbcTemplate){
        super();
        this.jdbcTemplate = jdbcTemplate;
    }


    public void identifyFilm(Film film) {
        String sqlQuery = "select count(*) from films where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, film.getId()) == 1;
        if (!isValid) throw new FilmIdentificationException("Film with ID " + film.getId() + " is not found");
    }


    public void identifyById(Long id) {
        String sqlQuery = "select count(*) from films where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) == 1;
        log.info("Film identification by ID: {}", isValid);
        if (!isValid) throw new FilmIdentificationException("Film with ID " + id + " is not found");
    }

    public void identifyUser(Long userId){
        String sqlQuery = "select count(*) from users where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId) == 1;
        log.info("User identification by ID: {}", isValid);
        if (!isValid) throw new UserIdentificationException("User with ID " + userId + " is not found");
    }

    public void validateFilmToCreate(Film film) {
        validateReleaseDate(film);
        validateDescription(film);
        validateDuration(film);
        validateName(film);

    }

    public void validateFilmToUpdate(Film film) {
        validateReleaseDate(film);
        validateDescription(film);
        validateDuration(film);
        identifyFilm(film);
    }

    public void validateDuration(Film film) {
        log.info("Duration validation: {}", film.getDuration() < 0);
        if (film.getDuration() < 0) throw new FilmDurationValidationException("Duration value should be positive");
    }

    public void validateReleaseDate(Film film){
        boolean isReleaseDateValid = LocalDate.parse(film.getReleaseDate(), formatter)
                .isAfter(LocalDate.of(1895, 12, 28));
        log.info("Release date validation: {}", isReleaseDateValid);
        if (!isReleaseDateValid) throw new ReleaseDateValidationException("Film release date should not be earlier " +
                "than December 28th of 1895");
    }

    public void validateDescription(Film film){
        if (film.getDescription().length() > 200)
            throw new DescriptionValidationException("Film description should not be more than 200 symbols");
    }

    public void validateName(Film film){
        boolean isValid = !film.getName().isEmpty() && !film.getName().isBlank();
        if (!isValid) throw new FilmNameValidationException("Film name cannot be empty");
    }


}
