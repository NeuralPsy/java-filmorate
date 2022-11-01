package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage{
    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User addFriend(Long id, Long friendId) {
        return null;
    }

    @Override
    public Long removeFriend(Long id, Long friendId) {
        return null;
    }

    @Override
    public List<Long> getFriendList(Long id) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public List<Long> getCommonFriends(Long id, Long otherId) {
        return null;
    }
}
