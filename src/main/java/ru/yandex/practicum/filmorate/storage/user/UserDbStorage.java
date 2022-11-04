package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    private final UserValidation userValidation;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public UserDbStorage (JdbcTemplate jdbcTemplate, @Qualifier("dbUserValidation") UserValidation userValidation){
        this.jdbcTemplate = jdbcTemplate;
        this.userValidation = userValidation;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select * from users;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User addUser(User user) {
        userValidation.validateUserToCreate(user);
        String sqlQuery = "insert into users (email, login, name, birthday, last_update)" +
                "values (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String lastUpdate = LocalDate.now().format(formatter);

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getBirthday());
            stmt.setString(5, lastUpdate);
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        user.setLastUpdate(LocalDate.parse(lastUpdate, formatter));

        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info(user.getEmail() + " updating");
        userValidation.validateUserToUpdate(user);
        String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ?, last_update = ? " +
                "where id = ?;";
        String lastUpdate = LocalDate.now().format(formatter);
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), lastUpdate);
        user.setLastUpdate(LocalDate.parse(lastUpdate, formatter));
        return user;
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        userValidation.identifyUserId(friendId);
        userValidation.identifyUserId(id);
        String lastUpdate = LocalDate.now().format(formatter);
        String sqlQuery = "insert into friendlist set user_id = ?, friend_id = ?, status = ?, last_update = ?;";
        jdbcTemplate.update(sqlQuery,id, friendId, 1, lastUpdate);
        return true;
    }

    @Override
    public boolean removeFriend(Long id, Long friendId) {
        userValidation.identifyUserId(friendId);
        userValidation.identifyUserId(id);
        String sqlQuery = "delete from friendlist where user_id = ?, friend_id = ?;";
        jdbcTemplate.update(sqlQuery,id, friendId);
        return true;
    }

    @Override
    public List<Long> getFriendList(Long id) {
        userValidation.identifyUserId(id);
        String sqlQuery = "select friend_id from friendlist where user_id = ?;";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, id);
    }

    @Override
    public User getUserById(Long id) {
        userValidation.identifyUserId(id);
        String sqlQuery = "select * from users where id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public List<Long> getCommonFriends(Long id, Long otherId) {
        String sqlQuery = "select friend_id from friendlist where user_id in (?, ?) " +
                "group by friend_id having count(*) = ?;";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, id, otherId, 2);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getString("birthday"))
                .lastUpdate(LocalDate.parse(rs.getString("last_update"), formatter))
                .build();

    }
}
