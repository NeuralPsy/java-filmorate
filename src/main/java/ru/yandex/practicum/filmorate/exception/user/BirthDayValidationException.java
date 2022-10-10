package ru.yandex.practicum.filmorate.exception.user;

import javax.validation.ValidationException;

public class BirthDayValidationException extends ValidationException {
    public BirthDayValidationException(String s) {
        super(s);
    }
}
