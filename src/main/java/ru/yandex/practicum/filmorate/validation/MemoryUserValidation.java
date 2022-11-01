package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.user.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
public class MemoryUserValidation implements UserValidation{

    private Map<Long, User> users;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MemoryUserValidation(Map<Long, User> users){
        this.users = users;
    }


    public void validateUserToCreate(User user) {
        validateLogin(user);
        validateEmail(user);
        validateBirthday(user);
    }

    public void validateUserToUpdate(User user) {
        identifyUser(user);
        validateLogin(user);
        validateEmail(user);
        validateBirthday(user);
        validateUserId(user);
    }

    public void validateEmail(User user) {
        boolean isValid = user.getEmail().contains("@")
                && user.getEmail().contains(".")
                && !user.getEmail().contains(" ");
        log.info("Email validation: {}", isValid);
        if (!isValid) throw new EmailValidationException("Email is not correct");
    }

    public void validateLogin(User user){
        boolean isCorrectLogin = !user.getLogin().isBlank() && !user.getLogin().contains(" ");
        log.info("Login validation: {}", isCorrectLogin);
        if (!isCorrectLogin) throw new LoginValidationException("Login should not be empty or contain spaces");
    }

    public void validateBirthday(User user){
        boolean isCorrectBirthday = LocalDate.parse(user.getBirthday(), formatter).isBefore(LocalDate.now());
        log.info("Birthday validation: {}", user.getBirthday());
        if (!isCorrectBirthday) throw new BirthDayValidationException("Birthday cannot be after current date");
    }

    public void identifyUser(User user){
        boolean isIdentified = users.containsKey(user.getId());
        log.info("User identification: "+isIdentified);
        if (!isIdentified) throw new UserIdentificationException("User with ID " + user.getId() + " is not found");
    }

    public void identifyUserId(Long id){
        boolean isIdentified = users.containsKey(id);
        log.info("User identification by ID: "+isIdentified);
        if (!isIdentified) throw new UserIdentificationException("User with ID " + id + " is not found");
    }

    public void validateUserId(User user){
        Long userId = user.getId();
        boolean isValid = userId != null
                && userId > 0;
        if (!isValid) throw new UserIDValidationException("User ID is not correct");
    }
}
