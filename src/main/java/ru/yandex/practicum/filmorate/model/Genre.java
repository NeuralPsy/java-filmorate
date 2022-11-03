package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Genre {
    private Integer genre_id;
    private String name;
    private LocalDate lastUpdate;
}
