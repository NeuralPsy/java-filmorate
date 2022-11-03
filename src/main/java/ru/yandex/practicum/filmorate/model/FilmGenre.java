package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FilmGenre {
    private Long filmId;
    private Integer genreId;
    private LocalDate lastUpdate;
}
