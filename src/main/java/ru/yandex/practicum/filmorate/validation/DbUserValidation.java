package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.user.UserIdentificationException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Component("dbUserValidation")
public class DbUserValidation extends MemoryUserValidation{
    private final JdbcTemplate jdbcTemplate;


    public DbUserValidation(JdbcTemplate jdbcTemplate){
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void identifyUser(User user) {
        String sqlQuery = "select count(*) from users where id = ? and email = ? and login = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, user.getId(),
                user.getEmail(), user.getLogin())==1;
        log.info("User identification: "+isValid);
        if (!isValid) throw new UserIdentificationException("User with ID " + user.getId() + " is not found");

    }

    @Override
    public void identifyUserId(Long id) {
        String sqlQuery = "select count(*) from users where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id)==1;
        log.info("User identification by ID: "+isValid);
        if (!isValid) throw new UserIdentificationException("User with ID " + id + " is not found");

    }
}
