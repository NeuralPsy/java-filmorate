package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class MpaIdDoesNotExistException extends ValidationException {
    public MpaIdDoesNotExistException(String s){
        super(s);
    }
}
