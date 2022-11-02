package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.FilmIdentificationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component("filmDbStorage")
public class FIlmDbStorage implements FilmStorage{

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final JdbcTemplate jdbcTemplate;

    public FIlmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa_rating, last_update)" +
                "values (?, ?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setString(3, film.getReleaseDate());
            stmt.setString(4, String.valueOf(film.getDuration()));
            stmt.setString(5, String.valueOf(film.getMpaRating()));
            stmt.setString(6, lastUpdate);
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        film.setLastUpdate(lastUpdate);

        return film;
    }

    @Override
    public Long remove(Long filmId) {
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
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "update films set name = ?, release_date = ?, description = ?, duration = ?, " +
                "mpa_rating = ?, last_update = ? where id = ?;";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getReleaseDate(), film.getDescription(),
                film.getDuration(), film.getMpaRating(), lastUpdate, film.getId());

        return film;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "select * from films;";]
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getLong("duration"))
                .mpaRating(rs.getInt("mpa_rating"))
                .releaseDate(rs.getString("release_date"))
                .lastUpdate(rs.getString("last_update"))
                .build();

    }

    @Override
    public Film getById(Long filmId) {

        return null;
    }

    @Override
    public Integer likeFilm(Long filmId, Long userId) {
        return null;
    }

    @Override
    public Integer unlikeFilm(Long filmId, Long userId) {
        return null;
    }

}
