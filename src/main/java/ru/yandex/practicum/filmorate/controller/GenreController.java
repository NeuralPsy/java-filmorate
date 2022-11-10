package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

/**
 * This class is to work with Genre.class object by sending REST requests
 */
@RestController
@RequestMapping(value = "/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService){
        this.genreService = genreService;
    }

    /**
     * @return Collection of Genre.class objects according to database
     */
    @GetMapping
    public Collection<Genre> findAll(){
        return  genreService.findAll();
    }

    /**
     * @param genreId is identificator of genre row in database
     * @return Genre.class object with entered id
     */
    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable Integer genreId){
        return genreService.getGenreById(genreId);
    }
}
