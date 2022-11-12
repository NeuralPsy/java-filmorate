package ru.yandex.practicum.filmorate.exception.mpa;

import javax.validation.ValidationException;

public class MpaIdDoesNotExistException extends ValidationException {
    public MpaIdDoesNotExistException(String s){
        super(s);
    }
}
