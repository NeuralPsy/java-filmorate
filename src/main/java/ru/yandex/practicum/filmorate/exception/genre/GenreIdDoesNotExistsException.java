package ru.yandex.practicum.filmorate.exception.genre;

import javax.validation.ValidationException;

public class GenreIdDoesNotExistsException extends ValidationException {

    public GenreIdDoesNotExistsException(String s) {
        super(s);
    }
}
