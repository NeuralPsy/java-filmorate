package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendshipStatus {
    private Integer statusId;
    private String name;
    private String lastUpdate;
}
