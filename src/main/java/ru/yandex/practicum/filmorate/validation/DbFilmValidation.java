package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.model.Film;

@Component("dbFilmValidation")
@Slf4j
public class DbFilmValidation extends MemoryFilmValidation{

    private final JdbcTemplate jdbcTemplate;

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

    @Override
    public void identifyFilm(Film film) {
        String sqlQuery = "select count(*) from films where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, film.getId()) == 1;
        if (!isValid) throw new FilmIdentificationException("Film with ID " + film.getId() + " is not found");
    }

    @Override
    public void identifyById(Long id) {
        String sqlQuery = "select count(*) from films where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) == 1;
        log.info("Film identification by ID: {}", isValid);
        if (!isValid) throw new FilmIdentificationException("Film with ID " + id + " is not found");
    }

}
