package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class User {

    private Long id;

    @Email
    private String email;

    private String login;

    private String name;

    @NotNull
    private String birthday;

    private String lastUpdate;

}
