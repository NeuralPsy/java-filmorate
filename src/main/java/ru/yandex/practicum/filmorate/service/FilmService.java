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
        return storage.add(film);
    }

    public Film updateFilm(Film film){
        return storage.update(film);
    }

    public Integer removeFilm(Integer filmId){
        return storage.remove(filmId);
    }

    public Film getFilm(Integer filmId){
        return storage.getById(filmId);
    }

    public Integer likeFilm(Integer filmId, Integer userId){
        return storage.likeFilm(filmId, userId);
    }

    public Integer unlikeFilm(Integer filmId, Integer userId) {
        return storage.unlikeFilm(filmId, userId);
    }

    public List<Film> showTopFilms(Integer count) {
        return storage.findAll().stream()
                .sorted((post1, post2) -> {
                    int comp = post1.getLikesCount().compareTo(post2.getLikesCount());
                    return comp;
                }).limit(count).collect(Collectors.toList());
    }
}
