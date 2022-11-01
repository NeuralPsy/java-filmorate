package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component("filmDbStorage")
public class FIlmDbStorage implements FilmStorage{

    private final JdbcTemplate jdbcTemplate;

    public FIlmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        Long id = film.getId();
        String name = film.getName();
        String description = film.getDescription();
        String releaseDate = film.getReleaseDate();
        Long duration = film.getDuration();
        Integer mpaRating = film.getMpaRating();
        String lastUpdate = film.getLastUpdate();

        String sql = "insert into films (id, name, description, release_date, duration, mpa_rating, last_update)" +
                "values ("+id+", "+name+", "+description+", " +
                ""+releaseDate+", "+duration+", "+mpaRating+", "+lastUpdate+");";

        jdbcTemplate.execute(sql);

        return film;
    }

    @Override
    public Long remove(Long filmId) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public List<Film> findAll() {
        return null;
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

    @Override
    public void identifyById(Long id) {

    }
}
