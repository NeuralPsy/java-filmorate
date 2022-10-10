package ru.yandex.practicum.filmorate.exception.user;

import javax.validation.ValidationException;

public class UserIDValidationException extends ValidationException {
    public UserIDValidationException(String user_id_is_not_correct) {
        super(user_id_is_not_correct);
    }
}
