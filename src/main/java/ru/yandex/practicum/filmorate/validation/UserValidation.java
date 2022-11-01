package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.user.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public interface UserValidation {
    void validateUserToCreate(User user);

    void validateUserToUpdate(User user);

    void validateEmail(User user);

    void validateLogin(User user);

    void validateBirthday(User user);

    void identifyUser(User user);

    void identifyUserId(Long id);

    void validateUserId(User user);
}
