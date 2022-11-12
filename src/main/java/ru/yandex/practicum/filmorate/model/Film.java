package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode
public class Film {

    private long id;

    @NotBlank
    private String name;

    @Size(min=0, max=200)
    private String description;

    @NotEmpty
    private String releaseDate;

    @Positive
    private long duration;

    private Mpa mpa;

    private Set<Genre> genres;

    @EqualsAndHashCode.Include
    private Long likesCount;

    private String lastUpdate;

}
