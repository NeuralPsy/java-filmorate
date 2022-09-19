package ru.yandex.practicum.filmorate.model;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class User {

    int id;
    @EqualsAndHashCode.Include
    String email;

    @EqualsAndHashCode.Include
    String login;

    String name;
    LocalDate birthday;


}
