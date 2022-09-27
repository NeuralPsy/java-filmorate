package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private int id;

    @NotBlank
    private String name; //проверка длины имени по ТХ не нужна

    @Size(min=0, max=200) //проверка длины описания
    private String description;

    @NotEmpty
    @NonNull
    private String releaseDate;

    @Positive
    private long duration;


}
