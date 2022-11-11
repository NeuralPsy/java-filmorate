package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.user.BirthDayValidationException;
import ru.yandex.practicum.filmorate.exception.user.EmailValidationException;
import ru.yandex.practicum.filmorate.exception.user.LoginValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    @Autowired
    private UserController userController;
    private User user;

    @BeforeEach
    void createUser(){
        user  = User.builder().build();
    }

    @Test
    void createUserWithoutNameTest(){
        user.setEmail("iggy@yahoo.com");
        user.setLogin("jasoniggy");
        user.setBirthday("1989-08-28");
        User user2 = userController.create(user);
        Assertions.assertEquals("jasoniggy", user2.getName());
    }

    @Test
    void createUserWithoutLogin() throws LoginValidationException{
        user.setEmail("iggy@yahoo.com");
        user.setBirthday("1989-08-28");
        user.setName("Jason Beautiquer");
        user.setLogin("");
        LoginValidationException exception = Assertions.assertThrows(LoginValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Login should not be empty or contain spaces", exception.getMessage());
    }

    @Test
    void createUserWithWrongEmailThatHasNoAtSign() throws EmailValidationException{
        user.setEmail("iggyyahoo.com");
        user.setBirthday("1989-08-28");
        user.setName("Jason Beautiquer");
        user.setLogin("jasoniggy");
        EmailValidationException exception = Assertions.assertThrows(EmailValidationException.class,
                () -> userController.create(user));
        assertEquals("Email is not correct", exception.getMessage());
    }

    @Test
    void createUserWithWrongEmailThatHasNoDot() throws EmailValidationException{
        user.setEmail("iggy@yahoocom");
        user.setBirthday("1989-08-28");
        user.setName("Jason Beautiquer");
        user.setLogin("jasoniggy");
        EmailValidationException exception = Assertions.assertThrows(EmailValidationException.class,
                () -> userController.create(user));
        assertEquals("Email is not correct", exception.getMessage());
    }

    @Test
    void createUserWithWrongEmailThatHasSpace() throws EmailValidationException{
        user.setEmail("ig gy@yahoo.com");
        user.setBirthday("1989-08-28");
        user.setName("Jason Beautiquer");
        user.setLogin("jasoniggy");
        EmailValidationException exception = Assertions.assertThrows(EmailValidationException.class,
                () -> userController.create(user));
        assertEquals("Email is not correct", exception.getMessage());
    }

    @Test
    void createUserWithWrongBirthday(){
        user.setEmail("iggy@yahoo.com");
        user.setBirthday("2990-08-28");
        user.setName("Jason Beautiquer");
        user.setLogin("jasoniggy");
        BirthDayValidationException exception = Assertions.assertThrows(BirthDayValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Birthday cannot follow by current date", exception.getMessage());
    }

    @Test
    void createUserWithLoginThatContainsSpace(){
        user.setEmail("iggy@yahoo.com");
        user.setBirthday("1989-08-28");
        user.setName("Jason Beautiquer");
        user.setLogin("pimple popper");
        LoginValidationException exception = Assertions.assertThrows(LoginValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Login should not be empty or contain spaces", exception.getMessage());
    }
}