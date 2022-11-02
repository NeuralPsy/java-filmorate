package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;
import ru.yandex.practicum.filmorate.validation.MemoryFilmValidation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class implements FilmStorage interface to work with Film class objects in storage
 */
@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage{
    private FilmValidation validation;

    private Map<Long, Film> films = new HashMap<>();

    private static int id = 1;

    /**
     * @return list of all existing films in storage as Film class objects as it requested via FilmController class
     */
    public List<Film> findAll(){
        return new ArrayList<>(films.values());
    }

    /**
     * @param film is object of Film class sent from create(Film film) method of FilmController class
     *             and put into add(Film film) method of FilmService class
     *             The object should have right format therefore it needs to be validated.
     *             If one of object properties is invalid, an exception is thrown
     * @exception DescriptionValidationException
     * @exception FilmDurationValidationException
     * @exception FilmIdentificationException
     * @exception FilmNameValidationException
     * @exception ReleaseDateValidationException
     * @return film object if its validated and no exception were thrown
     */
    @Override
    public Film addFilm(Film film){
        this.validation = new MemoryFilmValidation(films);
        validation.validateFilmToCreate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    /**
     * @param filmId is an ID of a film sent from remove(Long filmId) method of FilmController class
     *               and put into removeFilm(Long filmId) method of FilmService class as parameter
     * @exception FilmIdentificationException
     * @return the ID of film if its validated and exception was not thrown
     */
    @Override
    public Long remove(Long filmId) {
        this.validation = new MemoryFilmValidation(films);
        validation.identifyById(filmId);
        films.remove(filmId);
        return filmId;
    }

    /**
     * @param film is object of Film class sent from update(Film film) method of FilmController class
     *                  The object should have right format therefore it needs to be validated.
     *                  If one of object properties is invalid, an exception is thrown
     * @exception DescriptionValidationException
     * @exception FilmDurationValidationException
     * @exception FilmIdentificationException
     * @exception FilmNameValidationException
     * @exception ReleaseDateValidationException
     * @return the ID of film if its validated and exception was not thrown
     */
    @Override
    public Film update(Film film){
        this.validation = new MemoryFilmValidation(films);
        validation.validateFilmToUpdate(film);
        films.put(film.getId(), film);
        return film;
    }

    /**
     * @param filmId is an ID of a film sent from getFilm(Long filmId) method of FilmController class
     *               and put into getFilm(Long filmId) as argument of FilmService class
     * @exception FilmIdentificationException
     * @return the ID of film if its validated and no exception was thrown
     */
    @Override
    public Film getById(Long filmId){
        this.validation = new MemoryFilmValidation(films);
        validation.identifyById(filmId);
        return films.get(filmId);
    }

    /**
     * @param filmId is an ID of a film sent from likeFilm(Long filmId, Long userId) method of FilmController class
     *               and put into likeFilm(Long filmId, Long userId) method as argument of FilmService class
     * @param userId is an ID of a user sent from likeFilm(Long filmId, Long userId) method of FilmController class
     *               and put into likeFilm(Long filmId, Long userId) method as argument of FilmService class
     * @exception FilmIdentificationException
     * @return count of film likes is returned
     */
    @Override
    public boolean likeFilm(Long filmId, Long userId){
        this.validation = new MemoryFilmValidation(films);
        validation.identifyById(filmId);
        films.get(filmId).like(userId);
        return true;
    }

    /**
     * @param filmId filmId is an ID of a film sent from unlikeFilm(Long filmId, Long userId) method of
     *               FilmController class and put into unlikeFilm(Long filmId, Long userId) method as argument
     *               of FilmService class
     * @param userId is an ID of a user sent from unlikeFilm(Long filmId, Long userId) method of FilmController class
     *               and put into unlikeFilm(Long filmId, Long userId) method as argument of FilmService class
     * @exception FilmIdentificationException
     * @exception NotPossibleToUnlikeFilmException
     * @return count of film likes is returned
     */
    @Override
    public boolean unlikeFilm(Long filmId, Long userId){
        this.validation = new MemoryFilmValidation(films);
        validation.identifyById(filmId);
        validation.identifyUserByIdInFilm(filmId, userId);
        films.get(filmId).unlike(userId);
        return true;
    }
}
