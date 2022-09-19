package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EmailAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user){
        validateUser(user);
        return user;
    }

    private void validateUser(User user)
            throws EmailAlreadyExistsException, InvalidBirthdayException,
            InvalidEmailException, InvalidLoginException {

        checkIfEmailExists(user);
        validateLogin(user);
        validateEmail(user);
        validateBirthday(user);
    }

    private void validateLogin(User user){
        boolean isCorrectLogin = users
                .values()
                .stream()
                .noneMatch(user1 -> user.getLogin().equals(user1.getLogin()));

        if (!isCorrectLogin) throw new InvalidLoginException("This login already exists");
    }

    private void validateEmail(User user){
        boolean isCorrectEmail = !user.getEmail().equals(null)
                && user.getEmail().contains("@")
                && !user.getEmail().equals("");

        if (!isCorrectEmail) throw new InvalidEmailException("Email you entered is incorrect");
    }

    private void validateBirthday(User user){
        boolean isCorrectBirthday = user.getBirthday().isBefore(LocalDate.now());
        if (!isCorrectBirthday) throw new InvalidBirthdayException("Birthday cannot be later than current date");
    }

    private void checkIfEmailExists(User user){
        boolean isExistingEmail = users
                .keySet()
                .stream()
                .anyMatch(email -> user.getEmail().equals(email));

        if (isExistingEmail) throw new EmailAlreadyExistsException("This email already exists");
    }




}
