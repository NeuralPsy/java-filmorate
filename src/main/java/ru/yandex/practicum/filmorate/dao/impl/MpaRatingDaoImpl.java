package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.MpaIdDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component("mpaRatingDaoImpl")
public class MpaRatingDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getMpaList() {
        String sqlQuery = "select * from mpa;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa getMpa(Integer ratingId) {
        validateMpa(ratingId);
        String sqlQuery = "select * from mpa where id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeMpa(rs), ratingId);
    }


    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .lastUpdate(rs.getString("last_update"))
                .build();
    }

    private void validateMpa(Integer ratindId) throws MpaIdDoesNotExistException {
        String sqlQuery = "select count(*) from mpa where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, ratindId) == 1;
        if (!isValid) throw new MpaIdDoesNotExistException("Count not find MPA rating with ID "+ratindId);
    }
}
