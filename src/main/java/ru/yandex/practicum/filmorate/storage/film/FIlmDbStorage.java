package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.impl.MpaRatingDaoImpl;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validation.DbFilmValidation;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class implements FilmStorage interface to work with Film class objects in database storage
 */
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


    /**
     * @param film is object of Film class sent from create(Film film) method of FilmController class
     *             and put into add(Film film) method of FilmService class
     *             The object should have right format therefore it needs to be validated.
     *             If one of object properties is invalid, an exception is thrown
     * @exception DescriptionValidationException
     * @exception FilmDurationValidationException
     * @exception FilmIdentificationException
     * @exception FilmNameValidationException
     * @exception ReleaseDateValidationException
     * @return film object if its validated and no exception were thrown
     */
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
    /**
     * @param filmId is an ID of a film sent from remove(Long filmId) method of FilmController class
     *               and put into removeFilm(Long filmId) method of FilmService class as parameter
     * @exception FilmIdentificationException
     * @return the ID of film if its validated and exception was not thrown
     */
    @Override
    public Long remove(Long filmId) {
        validation.identifyById(filmId);
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, filmId);

        return filmId;
    }

    /**
     * @param film is object of Film class sent from update(Film film) method of FilmController class
     *                  The object should have right format therefore it needs to be validated.
     *                  If one of object properties is invalid, an exception is thrown
     * @exception DescriptionValidationException
     * @exception FilmDurationValidationException
     * @exception FilmIdentificationException
     * @exception FilmNameValidationException
     * @exception ReleaseDateValidationException
     * @return the ID of film if its validated and exception was not thrown
     */
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

    /**
     * @return collection of all existing films in storage as Film class objects as it requested via FilmController class
     */
    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select * from films;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    /**
     * @param filmId is an ID of a film sent from getFilm(Long filmId) method of FilmController class
     *               and put into getFilm(Long filmId) as argument of FilmService class
     * @exception FilmIdentificationException
     * @return the ID of film if its validated and no exception was thrown
     */
    @Override
    public Film getById(Long filmId) {
        validation.identifyById(filmId);
        String sqlQuery = "select * from films where id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeFilm(rs), filmId);
    }

    /**
     * @param filmId is an ID of a film sent from likeFilm(Long filmId, Long userId) method of FilmController class
     *               and put into likeFilm(Long filmId, Long userId) method as argument of FilmService class
     * @param userId is an ID of a user sent from likeFilm(Long filmId, Long userId) method of FilmController class
     *               and put into likeFilm(Long filmId, Long userId) method as argument of FilmService class
     * @exception FilmIdentificationException
     * @return count of film likes is returned
     */
    @Override
    public boolean likeFilm(Long filmId, Long userId) {
        validation.identifyById(filmId);
        validation.identifyUser(userId);
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "insert into liked_films (film_id, user_id, last_update) values (?, ?, ?);";
        jdbcTemplate.update(sqlQuery, filmId, userId, lastUpdate);
        return true;
    }

    /**
     * @param filmId filmId is an ID of a film sent from unlikeFilm(Long filmId, Long userId) method of
     *               FilmController class and put into unlikeFilm(Long filmId, Long userId) method as argument
     *               of FilmService class
     * @param userId is an ID of a user sent from unlikeFilm(Long filmId, Long userId) method of FilmController class
     *               and put into unlikeFilm(Long filmId, Long userId) method as argument of FilmService class
     * @exception FilmIdentificationException
     * @exception NotPossibleToUnlikeFilmException
     * @return true if unlike was successfull
     */
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

    /**
     * Method allows getting top films according to number of likes
     * @param count is value sent from showTopFilms(Integer count) method of FilmController class
     * @return Collection of Film class objects is returned.
     * Number of films if list equals "count" value
     */
    @Override
    public Collection<Film> showTopFilms(Integer count) {
        List<Film> films = findAll()
                .stream()
                .sorted((film2, film1) -> film1.getLikesCount().compareTo(film2.getLikesCount()))
                .limit(count)
                .collect(Collectors.toList());

        Collections.reverse(films);


        return films.stream().collect(Collectors.toList());
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
