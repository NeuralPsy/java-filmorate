package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmValidation {

    void identifyUserByIdInFilm(Long filmId, Long userId);

    void validateFilmToCreate(Film film);

    void validateFilmToUpdate(Film film);

    void validateDuration(Film film);

    void validateReleaseDate(Film film);

    void validateDescription(Film film);

    void identifyFilm(Film film);

    void validateName(Film film);

    void identifyById(Long id);

    void identifyUser(Long userId);
}
