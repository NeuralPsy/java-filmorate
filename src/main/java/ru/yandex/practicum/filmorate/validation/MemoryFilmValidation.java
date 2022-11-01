package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
public class MemoryFilmValidation implements FilmValidation{

    private Map<Long, Film> films;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MemoryFilmValidation(Map<Long, Film> films){
        this.films = films;
    }



    public void identifyUserByIdInFilm(Long filmId, Long userId) {
        boolean isValid = films.get(filmId).getUsersWhoLiked().contains(userId);
        if(!isValid) throw new NotPossibleToUnlikeFilmException("User with ID "
                + userId + " didn't like the film. It is not possible to unlike");
    }

    public void validateFilmToCreate(Film film) {
        validateReleaseDate(film);
        validateDescription(film);
        validateDuration(film);
        validateName(film);

    }

    public void validateFilmToUpdate(Film film) {
        validateReleaseDate(film);
        validateDescription(film);
        validateDuration(film);
        identifyFilm(film);
    }

    public void validateDuration(Film film) {
        log.info("Duration validation: {}", film.getDuration() < 0);
        if (film.getDuration() < 0) throw new FilmDurationValidationException("Duration value should be positive");
    }

    public void validateReleaseDate(Film film){
        boolean isReleaseDateValid = LocalDate.parse(film.getReleaseDate(), formatter)
                .isAfter(LocalDate.of(1895, 12, 28));
        log.info("Release date validation: {}", isReleaseDateValid);
        if (!isReleaseDateValid) throw new ReleaseDateValidationException("Film release date should not be earlier " +
                "than December 28th of 1895");
    }

    public void validateDescription(Film film){
        if (film.getDescription().length() > 200)
            throw new DescriptionValidationException("Film description should not be more than 200 symbols");
    }

    public void identifyFilm(Film film){
        boolean isValid = films.containsKey(film.getId());
        log.info("Film identification: {}", isValid);
        if (!isValid) throw new FilmIdentificationException("Film with ID " + film.getId() + " is not found");
    }

    public void validateName(Film film){
        boolean isValid = !film.getName().isEmpty() && !film.getName().isBlank();
        if (!isValid) throw new FilmNameValidationException("Film name cannot be empty");
    }

    public void identifyById(Long id){
        boolean isValid = films.containsKey(id);
        log.info("Film identification by ID: {}", isValid);
        if (!isValid) throw new FilmIdentificationException("Film with ID " + id + " is not found");
    }
}
