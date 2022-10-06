package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    Integer addFriend(Integer id, Integer friendId);

    Integer removeFriend(Integer id, Integer friendId);

    List<Integer> getFriendList(Integer id);

    User getUserById(Integer id);

    List<Integer> getCommonFriends(Integer id, Integer otherId);
}
