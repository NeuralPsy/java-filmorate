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
    public Film getFilm(@PathVariable Integer filmId){
        return filmService.getFilm(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Integer likeFilm(@PathVariable Integer filmId, @PathVariable Integer userId){
        return filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Integer unlikeFilm(@PathVariable Integer filmId, @PathVariable Integer userId){
        return filmService.unlikeFilm(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) Integer count){
        return filmService.showTopFilms(count);
    }
}
