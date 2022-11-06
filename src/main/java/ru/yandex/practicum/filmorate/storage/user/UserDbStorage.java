package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Component("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage{

    private static long idToAdd = 1;

    private final JdbcTemplate jdbcTemplate;

    private final UserValidation userValidation;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public UserDbStorage (JdbcTemplate jdbcTemplate, @Qualifier("dbUserValidation") UserValidation userValidation){
        this.jdbcTemplate = jdbcTemplate;
        this.userValidation = userValidation;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "select * from users;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User addUser(User user) {
        userValidation.validateUserToCreate(user);
        String lastUpdate = LocalDate.now().format(formatter);
        if (user.getName().equals("") || user.getName().isBlank()) user.setName(user.getLogin());

        String sqlQuery = "insert into users (email, login, name, birthday, last_update)" +
                "values (?, ?, ?, ?, ?);";



        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), lastUpdate);

        Long id = jdbcTemplate.queryForObject("select id from users where email = ? and login = ? and birthday = ? and last_update = ?;",
                Long.class, user.getEmail(), user.getLogin(), user.getBirthday(), lastUpdate);
        user.setLastUpdate(lastUpdate);
        user.setId(id);

        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info(user.getEmail() + " updating");
        userValidation.validateUserToUpdate(user);
        userValidation.identifyUserId(user.getId());
        String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ?, last_update = ? where id = ?;";
        String lastUpdate = LocalDate.now().format(formatter);
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), lastUpdate, user.getId());
        user.setLastUpdate(lastUpdate);
        return user;
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        userValidation.identifyUserId(friendId);
        userValidation.identifyUserId(id);
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "insert into friendlist (user_id, friend_id, status, last_update)" +
                "values(?, ?, ?, ?);";
        jdbcTemplate.update(sqlQuery,id, friendId, false, lastUpdate);
        return true;
    }

    @Override
    public boolean removeFriend(Long id, Long friendId) {
        userValidation.identifyUserId(friendId);
        userValidation.identifyUserId(id);
        String sqlQuery = "delete from friendlist where user_id = ? and friend_id = ?;";
        jdbcTemplate.update(sqlQuery,id, friendId);
        return true;
    }

    public Collection<User> getFriendList(Long id) {
        userValidation.identifyUserId(id);
        String sqlQuery = "select * from users where id in(select friend_id from frienslist where user_id =?)";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), id);
    }


    @Override
    public User getUserById(Long id) {
        userValidation.identifyUserId(id);
        String sqlQuery = "select * from users where id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        String sqlQuery = "select * from users where id in (select friend_id from friendlist where user_id in (?, ?) " +
                "group by friend_id having count(*) = ?);";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), id, otherId, 2);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getString("birthday"))
                .lastUpdate(rs.getString("last_update"))
                .build();

    }
}
