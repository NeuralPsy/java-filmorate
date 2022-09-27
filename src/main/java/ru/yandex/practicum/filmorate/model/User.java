package ru.yandex.practicum.filmorate.model;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {

    int id;

    @Email
    String email;

    @NotEmpty
    String login;

    String name;

    @NotNull
    String birthday;


}
