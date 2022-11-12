package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Interface to work with storage of Film class objects
 */
public interface FilmStorage {

    Film addFilm(Film film) throws SQLException;

    Long remove(Long filmId);

    Film update(Film film);

    Collection<Film> findAll();

    Film getById(Long filmId);

    boolean likeFilm(Long filmId, Long userId);

    boolean unlikeFilm(Long filmId, Long userId);

    Long getLikesCount(Long filmId);

    Collection<Film> showTopFilms(Integer count);
}
