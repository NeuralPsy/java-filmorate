package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll(){
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws LoginValidationException, EmailValidationException,
            BirthDayValidationException{
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws LoginValidationException, EmailValidationException,
            BirthDayValidationException, UserIdentificationException, UserIDValidationException{
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Integer addFriend(@PathVariable Integer id, @PathVariable Integer friendId){
        return userService.addFriend(id, friendId);
    }
    @DeleteMapping("/{id}/friends/{friendId}")
    public Integer removeFriend(@PathVariable Integer id, @PathVariable Integer friendId){
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<Integer> getFriendList(@PathVariable Integer id){
        return userService.getFriendList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<Integer> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId){
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id){
        return userService.getUser(id);
    }
}
