package ru.yandex.practicum.filmorate.exception;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String this_email_already_exists) {
    }
}
