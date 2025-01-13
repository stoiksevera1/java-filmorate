package ru.yandex.practicum.filmorate.storage.mappers;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {

        Genre genre;
        genre = Genre.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .build();
        return genre;
    }
}