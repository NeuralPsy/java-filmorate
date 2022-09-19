package ru.yandex.practicum.filmorate.exception;

public class DurationIsNegativeException extends RuntimeException {
    public DurationIsNegativeException(String duration_should_not_be_negative) {
    }
}
