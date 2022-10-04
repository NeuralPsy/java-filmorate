package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private Map<Integer, Film> films = new HashMap<>();

    public List<Film> findAll(){
        return new ArrayList<>(films.values());
    }

    public Film add(Film film){
        log.info("Creating film {}", film.getName());
        validateFilmToCreate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film){
        log.info("Updating film {}", film.getName());
        validateFilmToUpdate(film);
        films.put(film.getId(), film);
        return film;
    }

}
