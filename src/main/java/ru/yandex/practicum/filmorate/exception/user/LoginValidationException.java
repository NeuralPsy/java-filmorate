package ru.yandex.practicum.filmorate.exception.user;

import javax.validation.ValidationException;

public class LoginValidationException extends ValidationException {
    public LoginValidationException(String s) {
        super(s);
    }
}
