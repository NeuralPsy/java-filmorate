package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
public class MpaRatingService {
    private final MpaDao mpaDao;

    public MpaRatingService(MpaDao mpaDao){
        this.mpaDao = mpaDao;
    }


    public Collection<Mpa> findAll(){
        return mpaDao.getMpaList();
    }

    public Mpa getMpa(Integer id) {
        return mpaDao.getMpa(id);
    }
}
