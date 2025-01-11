package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user;
        user = User.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .email(rs.getString(3))
                .login(rs.getString(4))
                .birthday(rs.getTimestamp(5).toLocalDateTime().toLocalDate())
                .build();
        return user;
    }
}
