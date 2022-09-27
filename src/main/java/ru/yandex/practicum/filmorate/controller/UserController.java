package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
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

    private Map<Integer, User> users = new HashMap<>();
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");

    private static int userId = 1;

    @GetMapping
    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        boolean isValid = validateUserToCreate(user);
        if (!isValid) throw new ValidationException();
        user.setId(userId++);
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        log.info("Creating user with ID: " + user.getId());
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user){
        log.info(user.getEmail() + " updating");
        boolean isValid = validateUserToUpdate(user);
        if (!isValid) throw new ValidationException();
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    private boolean validateUserToCreate(User user) {

        boolean isValid = !identifyUser(user)
                && validateLogin(user)
//                && validateEmail(user)
                && validateBirthday(user);
        return isValid;
    }

    private boolean validateUserToUpdate(User user) {
        boolean isValid = identifyUser(user)
                && validateLogin(user)
                && validateEmail(user)
                && validateBirthday(user)
                && validateUserId(user);
        return isValid;
    }

    private boolean validateEmail(User user) {
        boolean isValid = user.getEmail().contains("@")
                && user.getEmail().contains(".")
                && !user.getEmail().contains(" ");
        return isValid;
    }

    private boolean validateLogin(User user){
        boolean isCorrectLogin = !user.getLogin().isBlank() && !user.getLogin().contains(" ");
        log.info("Long validation: " + user.getLogin());
        return isCorrectLogin;
    }

    private boolean validateBirthday(User user){
        log.info(user.getBirthday());
        boolean isCorrectBirthday = LocalDate.parse(user.getBirthday(), formatter).isBefore(LocalDate.now());
        return isCorrectBirthday;
    }


    private boolean identifyUser(User user){
        boolean isIdentified = users.containsKey(user.getId());

        log.info("User identification: "+isIdentified);

        return isIdentified;
    }

    private boolean validateUserId(User user){
        Integer userId = user.getId();
        boolean isValid= !userId.equals(null)
                && userId > 0;

        return isValid;
    }




}
