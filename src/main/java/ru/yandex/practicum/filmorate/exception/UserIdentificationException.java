package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class UserIdentificationException extends ValidationException {
    public UserIdentificationException(String s) {
        super(s);
    }
}
