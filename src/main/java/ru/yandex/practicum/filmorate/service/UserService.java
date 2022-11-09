package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

/**
 * UserService is class that allows work with API requests (processed by FilmController)
 * to realize UserStorage methods.
 */
@Service
public class UserService {
    private final UserStorage storage;

    /**
     * @param storage is UserStorage object that needs to be entered as an argument
     *                to initialize storage field
     */
    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    /**
     * @return list of all existing users in storage as User class objects
     */
    public Collection<User> findAll() {
        return storage.getAllUsers();
    }

    /**
     * @param user is User class object sent from create(User user) method of UserController class
     * @return User class object if its validated and no exceptions are thrown
     */
    public User addUser(User user) {
        return storage.addUser(user);
    }

    /**
     * @param user is User class object sent from update(User user) method of UserController class
     * @return User class object if its validated and no exceptions are thrown
     */
    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    /**
     * @param id user ID entered from addFriend(Long id, Long friendId) method of UserController class
     * @param friendId user ID entered from addFriend(Long id, Long friendId) method of UserController class
     *                 used with this ID will be added into friend list if its existence in user storage is validated
     * @return ID of user added list
     */
    public boolean addFriend(Long id, Long friendId) {
        return storage.addFriend(id, friendId);
    }

    /**
     * @param id user ID entered from removeFriend(Long id, Long friendId) method of UserController class
     * @param friendId user ID entered from getCommonFriends(Long id, Long userId) method of UserController class
     *                 used with this ID will be deleted from friend list if its existence in user storage is validated
     * @return ID of user removed from friend list
     */
    public boolean removeFriend(Long id, Long friendId) {
        return storage.removeFriend(id, friendId);
    }

    /**
     * @param id user ID entered from getFriendList(Long id) method of UserController class
     * @return list of User class objects
     */

    public Collection<User> getFriendList(Long id) {
        return storage.getFriendList(id);
    }

    /**
     * @param id user ID entered from getCommonFriends(Long id, Long userId) method of UserController class
     * @param otherId user ID entered from getCommonFriends(Long id, Long userId) method of UserController class
     *                whose common friends user needs to get
     * @return list of User class objects
     */
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return storage.getCommonFriends(id, otherId);
    }

    /**
     * @param id user ID entered from getUser(Long id) method of UserController class
     * @return User class object if entered user with the ID exists in user storage
     */
    public User getUser(Long id) {
        return storage.getUserById(id);
    }
}
