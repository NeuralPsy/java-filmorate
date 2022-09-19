package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {

    Map<String, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        validateUserToCreate(user);
        users.put(user.getEmail(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user){
        validateUserToUpdate(user);
        users.put(user.getEmail(), user);
        return user;
    }

    private void validateUserToCreate(User user) throws EmailAlreadyExistsException, InvalidBirthdayException,
            InvalidEmailException, InvalidLoginException {

        checkEmailAbsence(user);
        validateLogin(user);
        validateEmail(user);
        validateBirthday(user);
    }

    private void validateUserToUpdate(User user) throws EmailAlreadyExistsException, InvalidBirthdayException,
            InvalidEmailException, InvalidLoginException, UserDoesNotExistException {
        checkEmailExistence(user);
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

    private void checkEmailAbsence(User user){
        boolean isEmailAbsent = users
                .keySet()
                .stream()
                .noneMatch(email -> user.getEmail().equals(email));

        if (!isEmailAbsent) throw new EmailAlreadyExistsException("This email already exists");
    }

    private void checkEmailExistence(User user){
        boolean emailDoesNotExist = users
                .keySet()
                .stream()
                .noneMatch(email -> user.getEmail().equals(email));

        if (emailDoesNotExist) throw new UserDoesNotExistException("This email already exists");
    }




}
