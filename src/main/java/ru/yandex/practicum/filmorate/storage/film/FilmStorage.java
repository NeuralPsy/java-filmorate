package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    Film add(Film film);

    Integer remove(Integer filmId);

    Film update(Film film);

    List<Film> findAll();

    Film getById(Integer filmId);

    Integer likeFilm(Integer filmId, Integer userId);

    Integer unlikeFilm(Integer filmId, Integer userId);
}
