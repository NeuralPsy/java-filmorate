package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component("dbFilmValidation")
@Slf4j
public class DbFilmValidation extends MemoryFilmValidation{

    private final JdbcTemplate jdbcTemplate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public DbFilmValidation(JdbcTemplate jdbcTemplate){
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void identifyUserByIdInFilm(Long filmId, Long userId) {
        String sqlQuery = "select count(*) from liked_films where film_id = ? and user_id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, userId)==1;
        if (!isValid) throw new NotPossibleToUnlikeFilmException("User with ID "
                + userId + " didn't like the film. It is not possible to unlike");

    }

//    @Override
//    public void validateFilmToCreate(Film film) {
//        validateReleaseDate(film);
//        validateDescription(film);
//        validateDuration(film);
//        validateName(film);
//    }

//    @Override
//    public void validateFilmToUpdate(Film film) {
//        validateReleaseDate(film);
//        validateDescription(film);
//        validateDuration(film);
//        identifyFilm(film);
//    }

//    @Override
//    public void validateDuration(Film film) {
//        log.info("Duration validation: {}", film.getDuration() < 0);
//        if (film.getDuration() < 0) throw new FilmDurationValidationException("Duration value should be positive");
//    }

//    @Override
//    public void validateReleaseDate(Film film) {
//        boolean isReleaseDateValid = LocalDate.parse(film.getReleaseDate(), formatter)
//                .isAfter(LocalDate.of(1895, 12, 28));
//        log.info("Release date validation: {}", isReleaseDateValid);
//        if (!isReleaseDateValid) throw new ReleaseDateValidationException("Film release date should not be earlier " +
//                "than December 28th of 1895");
//
//    }

//    @Override
//    public void validateDescription(Film film) {
//        if (film.getDescription().length() > 200)
//            throw new DescriptionValidationException("Film description should not be more than 200 symbols");
//    }

    @Override
    public void identifyFilm(Film film) {
        String sqlQuery = "select count(*) from films where film_id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, film.getId()) == 1;
        if (!isValid) throw new FilmIdentificationException("Film with ID " + film.getId() + " is not found");
    }

//    @Override
//    public void validateName(Film film) {
//        boolean isValid = !film.getName().isEmpty() && !film.getName().isBlank();
//        if (!isValid) throw new FilmNameValidationException("Film name cannot be empty");
//
//    }

    @Override
    public void identifyById(Long id) {
        String sqlQuery = "select count(*) from films where film_id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) == 1;
        log.info("Film identification by ID: {}", isValid);
        if (!isValid) throw new FilmIdentificationException("Film with ID " + id + " is not found");
    }

}
