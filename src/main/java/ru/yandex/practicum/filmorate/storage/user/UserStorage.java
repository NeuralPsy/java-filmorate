package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Interface to work with storage of User class objects
 */
public interface UserStorage {
    Collection<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    boolean addFriend(Long id, Long friendId);

    boolean removeFriend(Long id, Long friendId);

    Collection<User> getFriendList(Long id);


    User getUserById(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);

    Boolean getFriendshipStatus(Long userId, Long friendId);

    boolean setMutualFriendship(Long id, Long friendId, Boolean status);
}
