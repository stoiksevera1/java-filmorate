package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final GenreDbStorage genreDbStorage;


    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films(name, description, duration, release_date, id_rating)" + "values(?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setLong(3, film.getDuration());
            stmt.setTimestamp(4, Timestamp.valueOf(film.getReleaseDate().atStartOfDay()));
            if (film.getRating() != null) {
                validationMpa(film.getRating().getId());
                stmt.setLong(5, film.getRating().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            return stmt;
        };

        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        if (!film.getGenres().isEmpty()) {
            String sqlQueryGenres = "insert into genre_films(film_id, genre_id)" + "values(?, ?)";
            for (Genre genre : film.getGenres()) {
                validationGenre(genre.getId());
                jdbcTemplate.update(sqlQueryGenres, film.getId(), genre.getId());
            }

        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {


        Long idRating = Optional.ofNullable(film.getRating())
                .map(Rating::getId)
                .orElse(null);
        if (idRating != null) {
            validationMpa(idRating);
        }

        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, id_rating = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                idRating,
                film.getId());
        if (rowsUpdated == 0) {
            log.warn("Ошибка обновления фильма ID не найден");
            throw new NotFoundException("Фильм " + film.getId() + " не найден");
        }
        if (!film.getGenres().isEmpty()) {
            String sqlQueryGenres = "insert into genre_films(film_id, genre_id)" + "values(?, ?)";
            for (Genre genre : film.getGenres()) {
                validationGenre(genre.getId());
                jdbcTemplate.update(sqlQueryGenres, film.getId(), genre.getId());
            }

        }
        return film;
    }

    @Override
    public List<Film> getAllFilm() {
        List<Film> films;
        String sqlQuery = "SELECT * FROM films";
        try {
            films = jdbcTemplate.query(sqlQuery, filmRowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("СПИСОК ПУСТ");
            throw new NotFoundException("Список пуст");
        }

        for (Film film : films) {
            final String sqlQueryLikes = "SELECT user_id FROM likes WHERE film_id = ?";
            Long count = film.getId();
            film.setLikes(jdbcTemplate.queryForList(sqlQueryLikes, Long.class, count));
        }
        return films;
    }

    @Override
    public Film getFilmById(Long id) {

        Film film;
        String sqlQuery = "SELECT id, name, description, duration, release_date, id_rating FROM films WHERE id = ?";
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, filmRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка поиска фильма ID не найден");
            throw new NotFoundException("Film " + id + " не найден");
        }
        final String sqlQueryGenre = "SELECT DISTINCT genre_id FROM genre_films WHERE film_id = ?";
        final String sqlQueryLikes = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Long> listUserId = jdbcTemplate.queryForList(sqlQueryLikes, Long.class, id);
        List<Long> listId = jdbcTemplate.queryForList(sqlQueryGenre, Long.class, id);
        List<Genre> genresfilm = listId.stream()
                .map(idF -> Genre.builder().id(idF)
                        .name(genreDbStorage.getGenreById(idF).getName())
                        .build())
                .toList();
        assert film != null;
        film.setGenres(genresfilm);
        film.setLikes(listUserId);
        return film;
    }

    private void validationMpa(Long id) {
        final String sqlQueryMpa = "SELECT COUNT(*) FROM film_rating WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQueryMpa, Integer.class, id);
        Optional.ofNullable(count)
                .filter(c -> c > 0)
                .orElseThrow(() -> new ValidationException("mpa c с таким id не существует"));

    }

    private void validationGenre(Long id) {
        final String sqlQueryGenre = "SELECT COUNT(*) FROM genre WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQueryGenre, Integer.class, id);
        Optional.ofNullable(count)
                .filter(c -> c > 0)
                .orElseThrow(() -> new ValidationException("Жанра c с таким id не существует"));

    }

    public void addLikeUser(Long id, Long userId) {
        String sqlQueryLikes = "insert into likes(film_id, user_id)" + "values(?, ?)";
        jdbcTemplate.update(sqlQueryLikes, id, userId);

    }

    public void dellLikeUser(Long id, Long userId) {
        String sqlQueryLikes = "delete  from  likes WHERE film_id = ? and  user_id = ?";
        jdbcTemplate.update(sqlQueryLikes, id, userId);
    }
}
