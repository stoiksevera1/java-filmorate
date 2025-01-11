package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mappers.RatingRowMapper;

import java.util.Collection;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class RatingDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingRowMapper ratingRowMapper;

    public Collection<Rating> getAllRating() {
        Collection<Rating> ratings;
        String sqlQuery = "SELECT * FROM film_rating";
        try {
            ratings = jdbcTemplate.query(sqlQuery, ratingRowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("СПИСОК ПУСТ");
            throw new NotFoundException("Список пуст");
        }
        return ratings;
    }

    public Rating getRatingById(Long id) {
        Rating rating;

        String sqlQuery = "SELECT * FROM film_rating WHERE id = ?";
        try {
            rating = jdbcTemplate.queryForObject(sqlQuery, ratingRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка поиска ретинга ID не найден");
            throw new NotFoundException("Рейтинг" + id + " не найден");
        }

        return rating;
    }
}