package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.model.Film;

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
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private Map<Long, Film> films = new HashMap<>();
    private static final  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        log.info("Creating film {}", film.getName());
        validateFilmToCreate(film);
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
        identifyById(filmId);
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
        log.info("Updating film {}", film.getName());
        validateFilmToUpdate(film);
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
        identifyById(filmId);
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
    public Integer likeFilm(Long filmId, Long userId){
        identifyById(filmId);
        films.get(filmId).like(userId);
        return films.get(filmId).getLikesCount();
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
    public Integer unlikeFilm(Long filmId, Long userId){
        identifyById(filmId);
        identifyUserByIdInFilm(filmId, userId);
        films.get(filmId).unlike(userId);
        return films.get(filmId).getLikesCount();
    }

    private void identifyUserByIdInFilm(Long filmId, Long userId) {
        boolean isValid = films.get(filmId).getUsersWhoLiked().contains(userId);
        if(!isValid) throw new NotPossibleToUnlikeFilmException("User with ID "
                + userId + " didn't like the film. It is not possible to unlike");
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

    public void identifyById(Long id){
        boolean isValid = films.containsKey(id);
        log.info("Film identification by ID: {}", isValid);
        if (!isValid) throw new FilmIdentificationException("Film with ID " + id + " is not found");
    }

}
