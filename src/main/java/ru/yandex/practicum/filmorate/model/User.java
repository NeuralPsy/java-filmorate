package ru.yandex.practicum.filmorate.model;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
public class User {

    int id;
    @EqualsAndHashCode.Include
    @Email
    String email;

    @EqualsAndHashCode.Include
    String login;

    String name;
    LocalDate birthday;


}
