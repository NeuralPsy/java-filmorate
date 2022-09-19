package ru.yandex.practicum.filmorate.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String this_login_already_exists) {
    }
}
