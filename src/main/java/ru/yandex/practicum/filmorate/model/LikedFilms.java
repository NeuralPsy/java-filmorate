package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LikedFilms {
    private Long filmId;
    private Long userId;
    private LocalDate lastUpdate;
}
