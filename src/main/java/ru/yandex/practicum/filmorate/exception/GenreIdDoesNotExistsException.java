package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class GenreIdDoesNotExistsException extends ValidationException {

    public GenreIdDoesNotExistsException(String s) {
        super(s);
    }
}
