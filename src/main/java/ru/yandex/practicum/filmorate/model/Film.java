package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode
public class Film {

    private long id;

    @JsonIgnore
    private Set<Long> usersWhoLiked = new HashSet<>();

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



//    public Long like(Long userId) {
//        usersWhoLiked.add(userId);
//        return userId;
//    }
//
//    public Long unlike(Long userId) {
//        usersWhoLiked.remove(userId);
//        return userId;
//    }
//
//    public Integer getLikesCount(){
//        return usersWhoLiked.size();
//    }
}
