package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User addFriend(Long id, Long friendId);

    Long removeFriend(Long id, Long friendId);

    List<Long> getFriendList(Long id);

    User getUserById(Long id);

    List<Long> getCommonFriends(Long id, Long otherId);
}
