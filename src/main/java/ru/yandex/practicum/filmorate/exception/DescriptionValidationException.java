package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class DescriptionValidationException extends ValidationException {
    public DescriptionValidationException(String s) {
        super(s);
    }
}
