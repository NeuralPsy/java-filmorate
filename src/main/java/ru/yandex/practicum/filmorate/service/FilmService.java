package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

@Service
public class FilmService {
    private final FilmStorage storage;

    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }
}
