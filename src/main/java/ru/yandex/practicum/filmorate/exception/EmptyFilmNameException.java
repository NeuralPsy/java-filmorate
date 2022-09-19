package ru.yandex.practicum.filmorate.exception;

public class EmptyFilmNameException extends RuntimeException {
    public EmptyFilmNameException(String film_name_cannot_be_empty) {
    }
}
