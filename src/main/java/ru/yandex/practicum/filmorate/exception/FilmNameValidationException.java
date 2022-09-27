package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class FilmNameValidationException extends ValidationException {
    public FilmNameValidationException(String film_name_cannot_be_empty) {
        super(film_name_cannot_be_empty);
    }
}
