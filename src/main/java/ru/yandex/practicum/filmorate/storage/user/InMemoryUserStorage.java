package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.user.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.MemoryUserValidation;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The class implements UserStorage interface to work with Film class objects in storage
 */
@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage{

    private UserValidation validation;

    private Map<Long, User> users = new HashMap<>();

//    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static long idToAdd = 1;

    /**
     * @return list of all existing users in storage as User class objects
     */
    public List<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    /**
     * @param user is User class object sent from create(User user) method of UserController class
     *             and put in addUser(User user) method as argument in UserService class
     * @exception BirthDayValidationException
     * @exception EmailValidationException
     * @exception LoginValidationException
     * @return User class object if its validated and no exceptions are thrown
     */
    public User addUser(User user){
        this.validation = new MemoryUserValidation(users);
        validation.validateUserToCreate(user);
        user.setId(idToAdd++);
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
//        log.info("Creating user with ID: " + user.getId());
        users.put(user.getId(), user);
        return user;
    }

    /**
     * @param user is User class object sent from update(User user) method of UserController class
     *             and put in updateUser(User user) method as argument in UserService class
     * @exception BirthDayValidationException
     * @exception EmailValidationException
     * @exception LoginValidationException
     * @exception UserIDValidationException
     * @return User class object if its validated and no exceptions are thrown
     */
    public User updateUser(User user){
        this.validation = new MemoryUserValidation(users);
        log.info(user.getEmail() + " updating");
        validation.validateUserToUpdate(user);
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    /**
     * @param id user ID entered from addFriend(Long id, Long friendId) method of UserController class
     *           and put in addFriend(Long id, Long friendId) method as argument in UserService class
     * @param friendId user ID entered from addFriend(Long id, Long friendId) method of UserController class
     *           and put in addFriend(Long id, Long friendId) method as argument in UserService class
     * @exception UserIDValidationException may be thrown if any user ID of his potential friends ID is invalid
     * @return ID of user added list
     */
    public User addFriend(Long id, Long friendId){
        this.validation = new MemoryUserValidation(users);
        validation.identifyUserId(friendId);
        validation.identifyUserId(id);
        users.get(id).addFriend(users.get(friendId));
        users.get(friendId).addFriend(users.get(id));
        return users.get(friendId);
    }

    /**
     * @param id user ID entered from removeFriend(Long id, Long friendId) method of UserController class
     *           and put in removeFriend(Long id, Long friendId) method as argument in UserService class
     * @param friendId user ID entered from addFriend(Long id, Long friendId) method of UserController class
     *           and put in addFriend(Long id, Long friendId) method as argument in UserService class
     * @exception UserIDValidationException may be thrown if any user ID of his potential friends ID is invalid
     * @return ID of user removed from friend list
     */
    public Long removeFriend(Long id, Long friendId){
        this.validation = new MemoryUserValidation(users);
        validation.identifyUserId(friendId);
        validation.identifyUserId(id);
        users.get(id).removeFriend(friendId);
        users.get(friendId).removeFriend(id);
        return friendId;
    }

    /**
     * @param userId user ID entered from getFriendList(Long id) method of UserController class and put in
     *               from getFriendList(Long id) method as argument in UserService class
     * @exception UserIDValidationException may be thrown may be thrown if any user ID of his potential
     *            friends ID is invalid
     * @return list of User class objects
     */
    public List<Long> getFriendList(Long userId){
        this.validation = new MemoryUserValidation(users);
        validation.identifyUserId(userId);
        return users.get(userId).getFriendList();
    }

    /**
     * @param id user ID entered from getUser(Long id) method of UserController class and put into getUser(Long id)
     *           method as argument in UserService class
     * @exception UserIDValidationException may be thrown may be thrown if any user ID of his potential
     *            friends ID is invalid
     * @return User class object if entered user with the ID exists in user storage
     */
    public User getUserById(Long id){
        this.validation = new MemoryUserValidation(users);
        validation.identifyUserId(id);
        return users.get(id);
    }

    /**
     * @param id user ID entered from getCommonFriends(Long id, Long userId) method of UserController class
     *           and put as argument in getCommonFriends(Long id, Long otherId) method in UserService class
     * @param otherId user ID entered from getCommonFriends(Long id, Long userId) method of UserController class
     *                and put as argument in getCommonFriends(Long id, Long otherId) method in UserService class
     *                whose common friends user needs to get
     * @return list of User class objects
     */
    @Override
    public List<Long> getCommonFriends(Long id, Long otherId) {
        this.validation = new MemoryUserValidation(users);
        validation.identifyUserId(id);
        validation.identifyUserId(otherId);
        return users.get(id)
                .getFriendList()
                .stream()
                .filter(users.get(otherId).getFriendList()::contains)
                .collect(Collectors.toList());
    }
}
