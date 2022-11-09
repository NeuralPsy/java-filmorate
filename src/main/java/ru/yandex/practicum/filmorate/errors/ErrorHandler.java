package ru.yandex.practicum.filmorate.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.GenreIdDoesNotExistsException;
import ru.yandex.practicum.filmorate.exception.MpaIdDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.film.FilmIdentificationException;
import ru.yandex.practicum.filmorate.exception.film.NotPossibleToUnlikeFilmException;
import ru.yandex.practicum.filmorate.exception.user.UserIDValidationException;
import ru.yandex.practicum.filmorate.exception.user.UserIdentificationException;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(final ValidationException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({
            UserIDValidationException.class,
            FilmIdentificationException.class,
            UserIdentificationException.class,
            NotPossibleToUnlikeFilmException.class,
            GenreIdDoesNotExistsException.class,
            MpaIdDoesNotExistException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final ValidationException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(final Throwable e){
        return new ErrorResponse(e.getMessage());
    }


}
