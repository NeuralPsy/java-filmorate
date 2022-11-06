package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.List;

@Service
public class MpaRatingService {

    private final MpaRatingDao mpaRatingDao;

    public MpaRatingService(MpaRatingDao mpaRatingDao){
        this.mpaRatingDao = mpaRatingDao;
    }


    public Collection<MpaRating> findAll(){
        return mpaRatingDao.getMpaRatingsList();
    }

    public MpaRating getMpaRating(Integer id) {
        return mpaRatingDao.getMpaRating(id);
    }
}
