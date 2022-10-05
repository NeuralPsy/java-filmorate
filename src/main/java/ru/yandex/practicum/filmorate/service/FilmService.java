package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public List<Film> findAll(){
        return storage.findAll();
    }

    public Film addFilm(Film film){
        return storage.add(film);
    }

    public Film updateFilm(Film film){
        return updateFilm(film);
    }

    public Integer removeFilm(Integer filmId){
        return storage.remove(filmId);
    }

    public Film getFilm(Integer filmId){
        return storage.getById(filmId);
    }
}
