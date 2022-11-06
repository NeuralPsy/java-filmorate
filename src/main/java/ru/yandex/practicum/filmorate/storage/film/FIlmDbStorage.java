package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.FilmIdentificationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component("filmDbStorage")
public class FIlmDbStorage implements FilmStorage{

    private final FilmValidation filmValidation;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FIlmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("dbFilmValidation") FilmValidation filmValidation){
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidation = filmValidation;
    }

    @Override
    public Film addFilm(Film film) {
        filmValidation.validateFilmToCreate(film);
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa_rating, last_update)" +
                "values (?, ?, ?, ?, ?, ?);";

        jdbcTemplate.update(sqlQuery,film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpaRating(), lastUpdate);

        Long id = jdbcTemplate.queryForObject("select id from films where name = ? and description = ? " +
                        "and release_date = ? and duration = ? and last_update = ?;",
                Long.class, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getLastUpdate(), lastUpdate);

        film.setLastUpdate(lastUpdate);
        film.setId(id);

        return film;
    }

    @Override
    public Long remove(Long filmId) {
        filmValidation.identifyById(filmId);
        String sqlQuery = "delete from films where id = ?";
        try {
            jdbcTemplate.update(sqlQuery, filmId);
        } catch (DataAccessException e){
            throw new FilmIdentificationException("Film with ID " + filmId + " is not found");
        }

        return filmId;
    }

    @Override
    public Film update(Film film) {
        filmValidation.validateFilmToUpdate(film);
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "update films set name = ?, release_date = ?, description = ?, duration = ?, " +
                "mpa_rating = ?, last_update = ? where id = ?;";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getReleaseDate(), film.getDescription(),
                film.getDuration(), film.getMpaRating(), lastUpdate, film.getId());
        film.setLastUpdate(lastUpdate);
        return film;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "select * from films;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }


    @Override
    public Film getById(Long filmId) {
        filmValidation.identifyById(filmId);
        String sqlQuery = "select * from films where id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeFilm(rs), filmId);
    }

    @Override
    public boolean likeFilm(Long filmId, Long userId) {
        filmValidation.identifyById(filmId);
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "update liked_films set film_id = ?, user_id = ?, last_update = ?;";
        jdbcTemplate.update(sqlQuery, filmId, userId, lastUpdate);
        return true;
    }

    @Override
    public boolean unlikeFilm(Long filmId, Long userId) {
        filmValidation.identifyById(filmId);
        filmValidation.identifyUserByIdInFilm(filmId, userId);
        String sqlQuery = "delete from liked_films where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return true;
    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getLong("duration"))
                .mpaRating(rs.getByte("mpa_rating"))
                .releaseDate(rs.getString("release_date"))
                .lastUpdate(rs.getString("last_update"))
                .build();

    }

}
