package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FilmService is class that allows work with API requests (processed by FilmController)
 * to realize FilmStorage methods.
 */
@Service
public class FilmService {

    private final FilmStorage storage;

    /**
     * @param storage is FilmStorage object that needs to be entered as an argument
     *                to initialize storage field
     */
    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage) {
        this.storage = storage;
    }

    /**
     * @return list of all existing films in storage as Film class objects
     */
    public List<Film> findAll(){
        return storage.findAll();
    }

    /**
     * @param film is object of Film class sent from create(Film film) method of FilmController class
     *             The object should have right format therefore it needs to be validated.
     *             If one of object properties is invalid, an exception is thrown
     * @return film object if its validated and no exception were thrown
     */
    public Film addFilm(Film film){
        return storage.addFilm(film);
    }

    /**
     * Method updates film that is already in film storage
     * @param film is object of Film class sent from update(Film film) method of FilmController class
     *                  The object should have right format therefore it needs to be validated.
     *                  If one of object properties is invalid, an exception is thrown
     * @return film object if its validated and no exception was thrown
     */
    public Film updateFilm(Film film){
        return storage.update(film);
    }

    /**
     * Method removes film from film storage by its ID
     * @param filmId is an ID of a film sent from remove(Long filmId) method of FilmController class
     *               The ID should be validated.
     *               If there's no film that has this ID, an exception is thrown
     *
     * @return the ID of film if its validated and exception was not thrown
     */
    public Long removeFilm(Long filmId){
        return storage.remove(filmId);
    }

    /**
     * Method returns Film class object identified by its ID from film storage
     * @param filmId is an ID of a film sent from getFilm(Long filmId) method of FilmController class
     * @return the ID of film if its validated and no exception was thrown
     */
    public Film getFilm(Long filmId){
        return storage.getById(filmId);
    }

    /**
     * Method allows user to like film
     * @param filmId is an ID of a film sent from likeFilm(Long filmId, Long userId) method of FilmController class
     * @param userId is an ID of a user sent from likeFilm(Long filmId, Long userId) method of FilmController class
     * @return count of film likes is returned
     */
    public Integer likeFilm(Long filmId, Long userId){
        storage.likeFilm(filmId, userId);
        return storage.getById(filmId).getLikesCount();
    }

    /**
     * Method allows user to unlike film
     * @param filmId filmId is an ID of a film sent from unlikeFilm(Long filmId, Long userId) method of FilmController class
     * @param userId is an ID of a user sent from unlikeFilm(Long filmId, Long userId) method of FilmController class
     * @return count of film likes is returned
     */
    public boolean unlikeFilm(Long filmId, Long userId) {
        return storage.unlikeFilm(filmId, userId);
    }

    /**
     * Method allows getting top films according to number of likes
     * @param count is value sent from showTopFilms(Integer count) method of FilmController class
     * @return list of Film class objects is returned.
     * Number of films is list equals "count" value
     */
    public List<Film> showTopFilms(Integer count) {
            return storage.findAll()
                    .stream()
                    .sorted((film1, film2) -> film2.getLikesCount().compareTo(film1.getLikesCount()))
                    .limit(count)
                    .collect(Collectors.toList());
    }
}
