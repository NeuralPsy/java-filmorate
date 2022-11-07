package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    GenreDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenresList() {
        String sqlQuery = "select * from genres;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        String sqlQuery = "select * from genres where id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeGenre(rs), genreId);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .lastUpdate(rs.getString("last_update"))
                .build();
    }
}
