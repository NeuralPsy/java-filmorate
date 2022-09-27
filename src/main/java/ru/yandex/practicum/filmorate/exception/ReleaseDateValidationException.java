package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class ReleaseDateValidationException extends ValidationException {
    public ReleaseDateValidationException(String s) {
        super(s);
    }
}
