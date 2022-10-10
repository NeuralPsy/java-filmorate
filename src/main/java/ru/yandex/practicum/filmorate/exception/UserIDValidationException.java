package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class UserIDValidationException extends ValidationException {
    public UserIDValidationException(String user_id_is_not_correct) {
    }
}
