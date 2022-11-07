package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.Collection;

@RestController
@RequestMapping(value = "/mpa")
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @Autowired
    MpaRatingController (MpaRatingService mpaRatingService){
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping
    public Collection<Mpa> findAll(){
        return mpaRatingService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa getMpaRating(@PathVariable Integer id){
        return mpaRatingService.getMpa(id);
    }
}
