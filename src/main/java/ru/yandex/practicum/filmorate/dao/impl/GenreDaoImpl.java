package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.GenreIdDoesNotExistsException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    GenreDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sqlQuery = "select * from genres;";
        List<Genre> genres =  new ArrayList<>();

        jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs)).forEach(genre -> genres.add(genre));
        return genres;
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        validateGenreId(genreId);
        return getAllGenres().stream()
                .filter(genre -> genre.getId() == genreId)
                .collect(Collectors.toList())
                .get(0);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .lastUpdate(rs.getString("last_update"))
                .build();
    }

    private void validateGenreId(Integer genreId){
        String sqlQuery = "select count(*) from genres where id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sqlQuery, Integer.class, genreId) != 0 && genreId > 0;
        if (!isValid) throw new GenreIdDoesNotExistsException("Could not find genre with id "+ genreId);
    }
}
