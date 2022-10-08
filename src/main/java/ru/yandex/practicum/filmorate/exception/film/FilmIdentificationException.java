package ru.yandex.practicum.filmorate.exception.film;

import javax.validation.ValidationException;

public class FilmIdentificationException extends ValidationException {
    public FilmIdentificationException(String s) {
        super(s);
    }
}
