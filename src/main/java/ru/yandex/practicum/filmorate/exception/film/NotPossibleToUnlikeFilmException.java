package ru.yandex.practicum.filmorate.exception.film;

import javax.validation.ValidationException;

public class NotPossibleToUnlikeFilmException extends ValidationException {
    public NotPossibleToUnlikeFilmException(String s) {
        super(s);
    }
}
