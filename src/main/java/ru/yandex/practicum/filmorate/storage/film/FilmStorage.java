package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Film add(Film film);

    Film remove(Film film);

    Film update(Film film);
}
