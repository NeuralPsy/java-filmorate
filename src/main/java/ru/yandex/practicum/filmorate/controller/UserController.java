package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

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

    private Map<Integer, User> users = new HashMap<>();
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");

    private static int userId = 1;

    @GetMapping
    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
//        boolean isValid = validateUserToCreate(user);
        validateUserToCreate(user);
//        if (!isValid) throw new ValidationException();
        user.setId(userId++);
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        log.info("Creating user with ID: " + user.getId());
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user){
        log.info(user.getEmail() + " updating");
        validateUserToUpdate(user);
//        boolean isValid = validateUserToUpdate(user);
//        if (!isValid) throw new ValidationException();
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    private void validateUserToCreate(User user) {

//        boolean isValid = !identifyUser(user)
//                && validateLogin(user)
//                && validateEmail(user)
//                && validateBirthday(user);

         validateLogin(user);
         validateEmail(user);
         validateBirthday(user);
//        return isValid;
    }

    private void validateUserToUpdate(User user) {
//        boolean isValid = identifyUser(user)
//                && validateLogin(user)
//                && validateEmail(user)
//                && validateBirthday(user)
//                && validateUserId(user);
        identifyUser(user);
        validateLogin(user);
        validateEmail(user);
        validateBirthday(user);
        validateUserId(user);
//        return isValid;
    }

    private void validateEmail(User user) {
        boolean isValid = user.getEmail().contains("@")
                && user.getEmail().contains(".")
                && !user.getEmail().contains(" ");
        log.info("Email validation: {}", isValid);
        if (!isValid) throw new EmailValidationException("Email is not correct");
//        return isValid;
    }

    private void validateLogin(User user){
        boolean isCorrectLogin = !user.getLogin().isBlank() && !user.getLogin().contains(" ");
        log.info("Long validation: " + user.getLogin());
        if (!isCorrectLogin) throw new LoginValidationException("Login should not be empty or contain spaces");
        //return isCorrectLogin;
    }

    private void validateBirthday(User user){
        boolean isCorrectBirthday = LocalDate.parse(user.getBirthday(), formatter).isBefore(LocalDate.now());
        log.info("Birthday validation: {}", user.getBirthday());
        if (!isCorrectBirthday) throw new BirthDayValidationException("Birthday cannot be after current date");
//        return isCorrectBirthday;
    }


    private void identifyUser(User user){
        boolean isIdentified = users.containsKey(user.getId());
        log.info("User identification: "+isIdentified);
        if (!isIdentified) throw new UserIdentificationException("User with ID " + user.getId() + " is not found");

//        return isIdentified;
    }

    private void validateUserId(User user){
        Integer userId = user.getId();
        boolean isValid= !userId.equals(null)
                && userId > 0;
        if (!isValid) throw new UserIDValidationException("User ID is not correct");

//        return isValid;
    }




}
