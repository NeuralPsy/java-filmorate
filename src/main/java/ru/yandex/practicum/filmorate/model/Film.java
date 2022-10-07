package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private long id;

    private Set<Long> usersWhoLiked = new HashSet<>();

    @NotBlank
    private String name;

    @Size(min=0, max=200)
    private String description;

    @NotEmpty
    private String releaseDate;

    @Positive
    private long duration;

    public Long like(Long userId) {
        usersWhoLiked.add(userId);
        return userId;
    }

    public Long unlike(Long userId) {
        usersWhoLiked.remove(userId);
        return userId;
    }

    public Integer getLikesCount(){
        return usersWhoLiked.size();
    }
}
