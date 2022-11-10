package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.impl.MpaRatingDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validation.DbFilmValidation;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
public class FIlmDbStorage implements FilmStorage{

    private final DbFilmValidation validation;
    private final MpaRatingDaoImpl mpa;
    private final GenreDao genreDao;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FIlmDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("dbFilmValidation") DbFilmValidation validation,
                         @Qualifier("mpaRatingDaoImpl") MpaRatingDaoImpl mpa,
                         GenreDao genreDao){
        this.jdbcTemplate = jdbcTemplate;
        this.validation = validation;
        this.mpa = mpa;
        this.genreDao = genreDao;
    }


    @Override
    public Film addFilm(Film film) {
        validation.validateFilmToCreate(film);
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa, last_update)" +
                "values (?, ?, ?, ?, ?, ?);";

        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), lastUpdate);
        film.setLastUpdate(lastUpdate);

        Long id = jdbcTemplate.queryForObject("SELECT id from films order by id desc limit 1;", Long.class);

        film.setId(id);

        String sqlQuery2 = "insert into film_genre (film_id, genre_id) values (?, ?);";
        if (film.getGenres() == null || film.getGenres().size() == 0) return film;

        film.getGenres().forEach(genre -> jdbcTemplate.update(sqlQuery2, film.getId(), genre.getId()));

        return film;
    }

    @Override
    public Long remove(Long filmId) {
        validation.identifyById(filmId);
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, filmId);

        return filmId;
    }

    @Override
    public Film update(Film film) {
        validation.validateFilmToUpdate(film);
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "update films set name = ?, release_date = ?, description = ?, duration = ?, " +
                "mpa = ?, last_update = ? where id = ?;";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getReleaseDate(), film.getDescription(),
                film.getDuration(), film.getMpa().getId(), lastUpdate, film.getId());
        film.setLastUpdate(lastUpdate);
        updateFilmGenre(film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select * from films;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }


    @Override
    public Film getById(Long filmId) {
        validation.identifyById(filmId);
        String sqlQuery = "select * from films where id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeFilm(rs), filmId);
    }

    @Override
    public boolean likeFilm(Long filmId, Long userId) {
        validation.identifyById(filmId);
        validation.identifyUser(userId);
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "insert into liked_films (film_id, user_id, last_update) values (?, ?, ?);";
        jdbcTemplate.update(sqlQuery, filmId, userId, lastUpdate);
        return true;
    }

    @Override
    public boolean unlikeFilm(Long filmId, Long userId) {
        validation.identifyById(filmId);
        validation.identifyUser(userId);
        String sqlQuery = "delete from liked_films where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return true;
    }

    @Override
    public Long getLikesCount(Long filmId) {
        validation.identifyById(filmId);
        String sqlQuery = "select count(*) from liked_films where film_id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, Long.class, filmId);
    }

    @Override
    public Collection<Film> showTopFilms(Integer count) {
        List<Film> films = findAll()
                .stream()
                .sorted((film2, film1) -> film1.getLikesCount().compareTo(film2.getLikesCount()))
                .collect(Collectors.toList());

        Collections.reverse(films);


        return films.stream().limit(count).collect(Collectors.toList());
    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getLong("duration"))
                .mpa(mpa.getMpa(rs.getInt("mpa")))
                .genres(new HashSet<>(makeGenreList(rs)))
                .releaseDate(rs.getString("release_date"))
                .likesCount(getLikesCount(rs.getLong("id")))
                .lastUpdate(rs.getString("last_update"))
                .build();

    }

    private List<Genre> makeGenreList(ResultSet rs) throws SQLException {
        List<Genre> genres = new ArrayList<>();
            String sqlQuery = "select genre_id from film_genre where film_id = ?;";
            jdbcTemplate.query(sqlQuery,
                    (rsInGenres, rowNum) -> genreDao.getGenreById(rsInGenres.getInt("genre_id")),
                    rs.getLong("id")).forEach(genre -> genres.add(genre));
        return genres;
    }

    private void updateFilmGenre(Film film){
        String lastUpdate = LocalDate.now().format(formatter);
        String delete = "delete from film_genre where film_id =?;";
        jdbcTemplate.update(delete, film.getId());
        String sqlQuery2 = "insert into film_genre (film_id, genre_id, last_update) values (?, ?, ?);";
        if (film.getGenres() == null || film.getGenres().size() == 0) return;
        film.getGenres().forEach(genre -> jdbcTemplate.update(sqlQuery2, film.getId(), genre.getId(), lastUpdate));
    }


}
