package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.user.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component("dbUserValidation")
public class DbUserValidation implements UserValidation{
    private final JdbcTemplate jdbcTemplate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DbUserValidation(JdbcTemplate jdbcTemplate){
        super();
        this.jdbcTemplate = jdbcTemplate;
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
        if (!isCorrectBirthday) throw new BirthDayValidationException("Birthday cannot follow by current date");
    }

    public void identifyUser(User user) {
        String sqlQuery = "select count(*) from users where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, user.getId())==1;
        log.info("User identification: "+isValid);
        if (!isValid) throw new UserIdentificationException("User with ID " + user.getId() + " is not found");

    }

    public void identifyUserId(Long id) {
        String sqlQuery = "select count(*) from users where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id)==1;
        log.info("User identification by ID: "+isValid);
        if (!isValid) throw new UserIdentificationException("User with ID " + id + " is not found");

    }

    public void validateUserId(User user){
        Long userId = user.getId();
        boolean isValid = userId != null
                && userId > 0;
        if (!isValid) throw new UserIDValidationException("User ID is not correct");
    }
}
