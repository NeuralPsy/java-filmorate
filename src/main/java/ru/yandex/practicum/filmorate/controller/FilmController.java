package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

/**
 * FilmController is class that allows to send API requests.
 */
@RestController
@RequestMapping(value = "/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * @return list of all existing films in storage
     */
    @GetMapping
    public List<Film> findAll(){
        return filmService.findAll();
    }

    /**
     * @param film is object of Film class sent from frontend part of application.
     *             The object should have right format therefore it needs to be validated.
     *             If one of object properties is invalid, an exception is thrown
     * @return film object if its validated and no exception was thrown
     */
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    /**
     * Method adds film into film storage
     * @param film is object of Film class sent from frontend part of application.
     *             The object should have right format therefore it needs to be validated.
     *             If one of object properties is invalid, an exception is thrown
     * @return film object if its validated and no exception was thrown
     */

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    /**
     * Method updates film that is already in film storage
     * @param filmId is object of Long class sent from path line as a path variable.
     *             The object should needs to be validated.
     *             If there's no film that has this ID, an exception is thrown
     * @return the ID of film if its validated and exception was not thrown
     */

    @DeleteMapping("/{filmId}")
    public Long remove(@PathVariable Long filmId){
        return filmService.removeFilm(filmId);
    }

    /**
     * Method removes film by its ID from film storage
     * @param filmId is object of Long class sent from path line as a path variable.
     *             The object should be validated.
     *             If there's no film that has this ID, an exception is thrown
     * @return the ID of film if its validated and exception was not thrown
     */
    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable Long filmId){
        return filmService.getFilm(filmId);
    }


    /**
     * @param filmId is object of Long class sent from path line as a path variable.
     *      *             The object should be validated.
     *      *             If there's no film that has this ID, an exception is thrown
     * @param userId is object of Long class sent from path line as a path variable.
     *      *             The object should be validated.
     *      *             If there's no user that has this ID, an exception is thrown
     * @return count of film likes is returned
     */
    @PutMapping("/{filmId}/like/{userId}")
    public Integer likeFilm(@PathVariable Long filmId, @PathVariable Long userId){
        return filmService.likeFilm(filmId, userId);
    }

    /**
     * @param filmId is object of Long class sent from path line as a path variable.
     *      *             The object should be validated.
     *      *             If there's no film that has this ID, an exception is thrown
     * @param userId is object of Long class sent from path line as a path variable.
     *      *             The object should be validated.
     *      *             If there's no user that has this ID, an exception is thrown
     * @return count of film likes is returned
     */

    @DeleteMapping("/{filmId}/like/{userId}")
    public Integer unlikeFilm(@PathVariable Long filmId, @PathVariable Long userId){
        return filmService.unlikeFilm(filmId, userId);
    }

    /**
     * @param count is value entered from path line as a request parameter. The parameter is not required.
     *              If it is not in path line the default value is set as 10
     * @return list of Film class objects is returned
     */
    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) Integer count){
        return filmService.showTopFilms(count);
    }
}
