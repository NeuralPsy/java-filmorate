package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");

    private static int id = 1;

    @GetMapping
    public List<Film> findAll(){
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        boolean isValid = validateFilmToCreate(film);
        if (!isValid) throw new ValidationException();
        log.info("Creating film {}", film.getName());
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film {}", film.getName());
        boolean isValid = validateFilmToUpdate(film);
        if (!isValid) throw new ValidationException();
        films.put(film.getId(), film);
        return film;
    }

    private boolean validateFilmToCreate(Film film) {
        boolean isValid = validateReleaseDate(film)
                && !identifyFilm(film);
        return isValid;
    }

    private boolean validateFilmToUpdate(Film film) {
        boolean isValid  = validateReleaseDate(film) && identifyFilm(film);
        log.info("Film validation: {}", isValid);
        return isValid;
    }

    private boolean validateReleaseDate(Film film){
        boolean isReleaseDateValid = LocalDate.parse(film.getReleaseDate(), formatter)
                .isAfter(LocalDate.of(1895, 12, 28));
        log.info("Release date validation: {}", isReleaseDateValid);
        return isReleaseDateValid;
    }


    private boolean identifyFilm(Film film){
        boolean isValid = films.containsKey(film.getId());
        log.info("Film identification: {}", isValid);
        return isValid;
    }
}
