package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private Map<Long, User> users = new HashMap<>();

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
    private static long idToAdd = 1;

    public List<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    public User addUser(User user){
        validateUserToCreate(user);
        user.setId(idToAdd++);
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        log.info("Creating user with ID: " + user.getId());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user){
        log.info(user.getEmail() + " updating");
        validateUserToUpdate(user);
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    public User addFriend(Long id, Long friendId){
        identifyUserId(friendId);
        identifyUserId(id);
        users.get(id).addFriend(users.get(friendId));
        users.get(friendId).addFriend(users.get(id));
        return users.get(friendId);
    }

    public Long removeFriend(Long id, Long friendId){
        identifyUserId(friendId);
        identifyUserId(id);
        users.get(id).removeFriend(friendId);
        users.get(friendId).removeFriend(id);
        return friendId;
    }

    public List<Long> getFriendList(Long userId){
        identifyUserId(userId);
        return users.get(userId).getFriendList();
    }

    public User getUserById(Long id){
        identifyUserId(id);
        return users.get(id);
    }

    @Override
    public List<Long> getCommonFriends(Long id, Long otherId) {
        identifyUserId(id);
        identifyUserId(otherId);
        return users.get(id)
                .getFriendList()
                .stream()
                .filter(users.get(otherId).getFriendList()::contains)
                .collect(Collectors.toList());
    }


    private void validateUserToCreate(User user) {
        validateLogin(user);
        validateEmail(user);
        validateBirthday(user);
    }

    private void validateUserToUpdate(User user) {
        identifyUser(user);
        validateLogin(user);
        validateEmail(user);
        validateBirthday(user);
        validateUserId(user);
    }

    private void validateEmail(User user) {
        boolean isValid = user.getEmail().contains("@")
                && user.getEmail().contains(".")
                && !user.getEmail().contains(" ");
        log.info("Email validation: {}", isValid);
        if (!isValid) throw new EmailValidationException("Email is not correct");
    }

    private void validateLogin(User user){
        boolean isCorrectLogin = !user.getLogin().isBlank() && !user.getLogin().contains(" ");
        log.info("Login validation: {}", isCorrectLogin);
        if (!isCorrectLogin) throw new LoginValidationException("Login should not be empty or contain spaces");
    }

    private void validateBirthday(User user){
        boolean isCorrectBirthday = LocalDate.parse(user.getBirthday(), formatter).isBefore(LocalDate.now());
        log.info("Birthday validation: {}", user.getBirthday());
        if (!isCorrectBirthday) throw new BirthDayValidationException("Birthday cannot be after current date");
    }

    private void identifyUser(User user){
        boolean isIdentified = users.containsKey(user.getId());
        log.info("User identification: "+isIdentified);
        if (!isIdentified) throw new UserIdentificationException("User with ID " + user.getId() + " is not found");
    }

    private void identifyUserId(Long id){
        boolean isIdentified = users.containsKey(id);
        log.info("User identification by ID: "+isIdentified);
        if (!isIdentified) throw new UserIdentificationException("User with ID " + id + " is not found");
    }

    private void validateUserId(User user){
        Long userId = user.getId();
        boolean isValid = userId != null
                && userId > 0;
        if (!isValid) throw new UserIDValidationException("User ID is not correct");
    }


}
