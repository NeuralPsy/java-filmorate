package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MpaRating {
    private Integer ratingId;
    private String name;
    private LocalDate lastUpdate;
}
