package ru.yandex.practicum.filmorate.model;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class User {

    int id;

    @Email
    String email;

    String login;

    String name;

    @NotNull
    String birthday;

    private Set<Integer> likedMovies = new HashSet<>();

    private Set<Integer> friends = new HashSet<>();

    public Integer addFriend(Integer friendId){
        friends.add(friendId);
        return friendId;
    }

    public Integer removeFriend(Integer friendId){
        friends.remove(friendId);
        return friendId;
    }

    public List<Integer> getFriendList(){
        return new ArrayList<>(friends);
    }



}
