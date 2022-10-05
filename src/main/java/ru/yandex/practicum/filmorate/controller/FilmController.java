package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");

    private static int id = 1;

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll(){
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/films/{filmId}")
    public Integer remove(@PathVariable String filmId){
        return filmService.removeFilm(Integer.getInteger(filmId));
    }

    @GetMapping("/films/{filmId}")
    public Film getFilm(@PathVariable String filmId){
        return filmService.getFilm(Integer.valueOf(filmId));
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Integer likeFilm(@PathVariable String filmId, @PathVariable String userId){
        return filmService.likeFilm(Integer.valueOf(filmId), Integer.valueOf(userId));
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Integer unlikeFilm(@PathVariable String filmId, @PathVariable String userId){
        return filmService.unlikeFilm(Integer.valueOf(filmId), Integer.valueOf(userId));
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) Integer count){
        return filmService.showTopFilms(count);
    }
}
