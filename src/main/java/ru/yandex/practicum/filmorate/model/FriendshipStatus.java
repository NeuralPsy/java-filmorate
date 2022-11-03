package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FriendshipStatus {
    private Integer statusId;
    private String name;
    private LocalDate lastUpdate;
}
