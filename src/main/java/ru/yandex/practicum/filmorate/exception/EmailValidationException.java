package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class EmailValidationException extends ValidationException {
    public EmailValidationException(String email_is_not_correct) {
        super(email_is_not_correct);
    }
}
