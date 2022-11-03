package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friendlist {
    private Long user_id;
    private Long friend_id;
    private Integer status;
    private String lastUpdate;
}
