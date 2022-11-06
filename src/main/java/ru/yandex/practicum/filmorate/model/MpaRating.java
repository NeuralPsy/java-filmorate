package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MpaRating {
    private byte id;
    private String name;
    private String lastUpdate;
}
