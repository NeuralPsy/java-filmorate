package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

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

    @JsonIgnore
    private Set<Integer> likedMovies = new HashSet<>();

    @JsonIgnore
    private List<User> friends = new ArrayList<>();

    public User addFriend(User user){
        friends.add(user);
        return user;
    }

    public Long removeFriend(Long friendId){
        friends.remove(friendId);
        return friendId;
    }

    public List<User> getFriendList(){
        return new ArrayList<>(friends);
    }





}
