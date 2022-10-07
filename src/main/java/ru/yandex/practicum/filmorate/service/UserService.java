package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> findAll() {
        return storage.getAllUsers();
    }

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public User addFriend(Long id, Long friendId) {
        return storage.addFriend(id, friendId);
    }

    public Long removeFriend(Long id, Long friendId) {
        return storage.removeFriend(id, friendId);
    }

    public List<User> getFriendList(Long id) {
        return storage.getFriendList(id).stream().map(id0 -> storage.getUserById(id0)).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> users = new ArrayList<>();
        storage.getCommonFriends(id, otherId).forEach(commonId -> users.add(storage.getUserById(commonId)));
        return users;
    }

    public User getUser(Long id) {
        return storage.getUserById(id);
    }
}
