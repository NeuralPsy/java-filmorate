package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;

public interface MpaRatingDao {

    Collection<MpaRating> getMpaRatingsList();

    MpaRating getMpaRating(Integer ratingId);
}
