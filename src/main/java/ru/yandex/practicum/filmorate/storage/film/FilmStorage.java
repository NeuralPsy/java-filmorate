package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Long remove(Long filmId);

    Film update(Film film);

    List<Film> findAll();

    Film getById(Long filmId);

    Integer likeFilm(Long filmId, Long userId);

    Integer unlikeFilm(Long filmId, Long userId);

    void identifyById(Long id);

}
