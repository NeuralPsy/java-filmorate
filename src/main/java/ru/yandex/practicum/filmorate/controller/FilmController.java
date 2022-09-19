package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll(){
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film){
        log.info(film.getName());
        validateFilmToCreate(film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film){
        log.info(film.getName());
        validateFilmToUpdate(film);
        films.put(film.getId(), film);
        return film;
    }

    private void validateFilmToCreate(Film film) throws EmptyFilmNameException, DescriptionOverLengthException,
            ImpossiblyEarlyReleaseDateException, DurationIsNegativeException{
        validateFilmName(film);
        validateFilmDescription(film);
        validateFilmReleaseDate(film);
        validateFilmDuration(film);
    }

    private void validateFilmToUpdate(Film film) throws FilmIsNotInListException, EmptyFilmNameException,
            DescriptionOverLengthException, ImpossiblyEarlyReleaseDateException, DurationIsNegativeException{

        checkFilmExistence(film);
        validateFilmName(film);
        validateFilmDescription(film);
        validateFilmReleaseDate(film);
        validateFilmDuration(film);
    }

    private void validateFilmName(Film film){
        boolean isNameValid = !film.getName().equals(null) || !film.getName().equals("");

        log.info("Is film name valid: "+isNameValid);

        if (!isNameValid) throw new EmptyFilmNameException("Film name cannot be empty");
    }

    private void validateFilmDescription(Film film){
        boolean isDescriptionValid = film.getDescription().length() <= 200;

        log.info("Is description valid: "+isDescriptionValid);

        if (!isDescriptionValid) throw new DescriptionOverLengthException("Film description should not " +
                "take more than 200 symbols");
    }

    private void validateFilmReleaseDate(Film film){
        boolean isReleaseDateValid = film.getReleaseDate()
                .isAfter(LocalDate.of(1895, 12, 28));
        log.info("Is release date valid: "+ isReleaseDateValid);
        if (!isReleaseDateValid) throw new ImpossiblyEarlyReleaseDateException("Film release date cannot be earlier " +
                "than December 28th of 1895");
    }

    private void validateFilmDuration(Film film){
        log.info("Duration: "+ film.getDuration());
        if (film.getDuration() < 0) throw new DurationIsNegativeException("Duration should not be negative");
    }

    private void checkFilmExistence(Film film){
        log.info("Does film exist in list: "+films.containsKey(film.getId()));
        if (!films.containsKey(film.getId())) throw new FilmIsNotInListException("There is no film with this ID " +
                "("+film.getId()+")");
    }
}
