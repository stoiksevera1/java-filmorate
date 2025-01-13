package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor

public class FilmRowMapper implements RowMapper<Film> {
    private final RatingDbStorage ratingDbStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film;
        film = Film.builder()
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .description(rs.getString(3))
                .duration(rs.getLong(4))
                .releaseDate(rs.getTimestamp(5).toLocalDateTime().toLocalDate())
                .rating(Rating.builder()
                        .id(rs.getLong(6))
                        .name(rs.getString(7))
                        .build())
                .build();
        return film;
    }
}
