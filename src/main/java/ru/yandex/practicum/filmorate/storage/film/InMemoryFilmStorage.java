package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private Map<Integer, Film> films = new HashMap<>();
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");

    private static int id = 1;

    public List<Film> findAll(){
        return new ArrayList<>(films.values());
    }

    @Override
    public Film add(Film film){
        log.info("Creating film {}", film.getName());
        validateFilmToCreate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film remove(Film film) {
        identifyFilm(film);
        films.remove(film.getId());
        return film;
    }

    @Override
    public Film update(Film film){
        log.info("Updating film {}", film.getName());
        validateFilmToUpdate(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(Integer filmId){
        identifyFilm(films.get(filmId));
        return films.get(filmId);
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
        boolean isValid = !film.getName().isEmpty() && !film.getName().isBlank();
        if (!isValid) throw new FilmNameValidationException("Film name cannot be empty");
    }

}
