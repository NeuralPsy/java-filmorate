package ru.yandex.practicum.filmorate.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email_you_entered_is_incorrect) {
    }
}
