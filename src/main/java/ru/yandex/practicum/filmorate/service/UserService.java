package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
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

    public Integer addFriend(Integer id, Integer friendId) {
        return storage.addFriend(id, friendId);
    }

    public Integer removeFriend(Integer id, Integer friendId) {
        return storage.removeFriend(id, friendId);
    }

    public List<Integer> getFriendList(Integer id) {
        return storage.getFriendList(id);
    }

    public List<Integer> getCommonFriends(Integer id, Integer otherId) {
        return storage.getUserById(id)
                .getFriendList()
                .stream()
                .filter(storage.getUserById(otherId).getFriendList()::contains)
                .collect(Collectors.toList());
    }
}
