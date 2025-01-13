package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.Collection;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    public Collection<Genre> getAllGenre() {
        Collection<Genre> genres;
        String sqlQuery = "SELECT * FROM genre";
        try {
            genres = jdbcTemplate.query(sqlQuery, genreRowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("СПИСОК ПУСТ");
            throw new NotFoundException("Список пуст");
        }
        return genres;
    }

    public Genre getGenreById(Long id) {
        Genre genre;

        String sqlQuery = "SELECT * FROM genre WHERE id = ?";
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, genreRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка поиска жанра ID не найден");
            throw new NotFoundException("Жанр" + id + " не найден");
        }

        return genre;
    }
}
