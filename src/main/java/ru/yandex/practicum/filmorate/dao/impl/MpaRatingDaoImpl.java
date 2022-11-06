package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Component
public class MpaRatingDaoImpl implements MpaRatingDao {

    private final JdbcTemplate jdbcTemplate;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MpaRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MpaRating> getMpaRatingsList() {
        String sqlQuery = "select * from mpa_rating;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpaRating(rs));
    }

    @Override
    public MpaRating getMpaRating(Integer ratingId) {
        String sqlQuery = "select * from mpa_rating where id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeMpaRating(rs), ratingId);
    }


    private MpaRating makeMpaRating(ResultSet rs) throws SQLException {
        return MpaRating.builder()
                .id(rs.getByte("id"))
                .name(rs.getString("name"))
                .lastUpdate(rs.getString("last_update"))
                .build();
    }
}
