package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.*;

@Data
public class User {

    private Long id;

    @Email
    String email;

    String login;

    String name;

    @NotNull
    String birthday;

    String lastUpdate;

    private Set<Integer> likedMovies = new HashSet<>();

    private List<Long> friends = new ArrayList<>();

    public User addFriend(User user){
        friends.add(user.getId());
        return user;
    }

    public Long removeFriend(Long friendId){
        friends.remove(friendId);
        return friendId;
    }

    public List<Long> getFriendList(){
        return new ArrayList<>(friends);
    }



}
