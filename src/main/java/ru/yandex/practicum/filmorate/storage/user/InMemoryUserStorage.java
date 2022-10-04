package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.service.UserService;

@Component
public class InMemoryUserStorage implements UserStorage{

    private final UserService userService;

    public InMemoryUserStorage(UserService userService) {
        this.userService = userService;
    }
}
