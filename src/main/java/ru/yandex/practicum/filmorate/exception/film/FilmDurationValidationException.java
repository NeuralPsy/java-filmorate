package ru.yandex.practicum.filmorate.exception.film;

import javax.validation.ValidationException;

public class FilmDurationValidationException extends ValidationException {
    public FilmDurationValidationException(String duration_value_should_be_positive) {
        super(duration_value_should_be_positive);
    }
}
