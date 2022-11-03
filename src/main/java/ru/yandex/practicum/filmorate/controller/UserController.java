package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * UserController is class that allows to send API requests to realize UserService methods.
 */
@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    /**
     * @param userService is UserService object that needs to be entered as an argument
     *                    to initialize userService field
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return list of all existing users in storage as User class objects
     */
    @GetMapping
    public List<User> findAll(){
        return userService.findAll();
    }

    /**
     * @param user is User class object that is needed to be added in user storage
     * @return user if its validated and no exceptions are thrown
     */
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * @param user is User class object that is already in user storage and its needed to be updated
     * @return user if its validated and no exceptions are thrown
     */
    @PutMapping
    public User update(@Valid @RequestBody User user){
        return userService.updateUser(user);
    }

    /**
     * @param id is path variable representing user ID
     * @param friendId is path variable representing user friend's ID
     * @return ID of an added friend into friend list
     */
    @PutMapping("/{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable Long id, @PathVariable Long friendId){
        return userService.addFriend(id, friendId);
    }

    /**
     * @param id is path variable representing user ID
     * @param friendId is path variable representing user friend's ID
     * @return ID of a friend who was removed from friend list
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean removeFriend(@PathVariable Long id, @PathVariable Long friendId){
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendList(@PathVariable Long id){
        return userService.getFriendList(id);
    }

    /**
     * @param id is path variable representing user ID
     * @param otherId is path variable representing other user ID whose common friends user need to get
     * @return list of User class objects
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId){
        return userService.getCommonFriends(id, otherId);
    }

    /**
     * @param id is path variable representing user ID
     * @return User class object, if user of this ID exists in user storages
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        return userService.getUser(id);
    }
}
