package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
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
        log.info("Creating film {}", film.getName());
        validateFilmToCreate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film {}", film.getName());
        validateFilmToUpdate(film);
        films.put(film.getId(), film);
        return film;
    }

    private void validateFilmToCreate(Film film) {
        validateReleaseDate(film);
        validateDescription(film);
        validateDuration(film);
        validateName(film);

    }



    private void validateFilmToUpdate(Film film) {

        validateReleaseDate(film);
        validateDescription(film);
        validateDuration(film);
        identifyFilm(film);
    }

    private void validateDuration(Film film) {
        log.info("Duration validation: {}", film.getDuration() < 0);
        if (film.getDuration() < 0) throw new FilmDurationValidationException("Duration value should be positive");
    }

    private void validateReleaseDate(Film film){
        boolean isReleaseDateValid = LocalDate.parse(film.getReleaseDate(), formatter)
                .isAfter(LocalDate.of(1895, 12, 28));
        log.info("Release date validation: {}", isReleaseDateValid);
        if (!isReleaseDateValid) throw new ReleaseDateValidationException("Film release date should not be earlier " +
                "than December 28th of 1895");
    }

    private void validateDescription(Film film){
        if (film.getDescription().length() > 200)
            throw new DescriptionValidationException("Film description should not be more than 200 symbols");
    }

    private void identifyFilm(Film film){
        boolean isValid = films.containsKey(film.getId());
        log.info("Film identification: {}", isValid);
        if (!isValid) throw new FilmIdentificationException("Film with ID " + film.getId() + " is not found");
    }

    private void validateName(Film film){
        boolean isValid = !film.getName().isEmpty() && !film.getName().isBlank() && film.getName().equals(null);
        if (!isValid) throw new FilmNameValidationException("Film name cannot be empty");
    }
}
