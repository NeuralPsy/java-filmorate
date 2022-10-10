package ru.yandex.practicum.filmorate.exception.user;

import javax.validation.ValidationException;

public class UserIdentificationException extends ValidationException {
    public UserIdentificationException(String s) {
        super(s);
    }
}
