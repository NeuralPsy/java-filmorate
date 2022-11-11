package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class User {

    private Long id;

    @Email
    String email;

    String login;

    String name;

    @NotNull
    String birthday;

    String lastUpdate;

}
