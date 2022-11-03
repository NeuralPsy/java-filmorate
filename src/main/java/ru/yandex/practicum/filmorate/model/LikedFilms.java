package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikedFilms {
    private Long filmId;
    private Long userId;
    private String lastUpdate;
}
