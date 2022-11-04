package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {
    private final GenreDao genreDao;

    GenreService(GenreDao genreDao){
        this.genreDao = genreDao;
    }

    public List<Genre> findAll(){
        return genreDao.getGenresList();
    }

    public Genre getGenreById(Integer genreId){
        return genreDao.getGenreById(genreId);
    }
}
