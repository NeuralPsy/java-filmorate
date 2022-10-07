package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class NotPossibleToUnlikeFilm extends ValidationException {
    public NotPossibleToUnlikeFilm(String s) {
        super(s);
    }
}
