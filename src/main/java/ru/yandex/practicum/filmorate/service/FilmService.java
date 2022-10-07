package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

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
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film){
        return storage.update(film);
    }

    public Long removeFilm(Long filmId){
        return storage.remove(filmId);
    }

    public Film getFilm(Long filmId){
        return storage.getById(filmId);
    }

    public Integer likeFilm(Long filmId, Long userId){
        storage.likeFilm(filmId, userId);
        return storage.getById(filmId).getLikesCount();
    }

    public Integer unlikeFilm(Long filmId, Long userId) {
        return storage.unlikeFilm(filmId, userId);
    }

    public List<Film> showTopFilms(Integer count) {
            return storage.findAll()
                    .stream()
                    .sorted((film1, film2) -> film2.getLikesCount().compareTo(film1.getLikesCount()))
                    .limit(count)
                    .collect(Collectors.toList());
    }
}
