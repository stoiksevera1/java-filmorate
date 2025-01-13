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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Collection;

import java.util.List;
import java.util.Objects;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage, FriendshipUser {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public User add(User user) {
        String sqlQuery = "insert into users(name, email, login, birthday)" + "values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setTimestamp(4, Timestamp.valueOf(user.getBirthday().atStartOfDay()));
            return stmt;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), Timestamp.valueOf(user.getBirthday().atStartOfDay()), user.getId());
        if (rowsUpdated == 0) {
            log.warn("Ошибка обновления пользователя ID не найден");
            throw new NotFoundException("Пользователь " + user.getId() + " не найден");
        }
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sqlQuery, userRowMapper);
        if (users.isEmpty()) {
            throw new NotFoundException("Список пуст");
        }

        return users;
    }

    @Override
    public User getUser(Long id) {
        String sqlQuery = "SELECT id, name, email, login, birthday FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка поиска пользователя ID не найден");
            throw new NotFoundException("Пользователь " + id + " не найден");
        }
    }

    public void addFriend(Long id, Long friendId, String status) {
        String sqlQueryAddFriend = "INSERT INTO friends(user_id, friend_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQueryAddFriend, id, friendId, status);
    }

    public void dellFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE  FROM friends WHERE friend_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, friendId, id);
    }

    @Override
    public Collection<Long> getFriendsUser(Long id) {
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, id);
    }


    public boolean checkFriendship(Long friendId, Long id) {
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?";
        List<Long> idFriends = jdbcTemplate.queryForList(sqlQuery, Long.class, friendId);
        return idFriends.contains(id);
    }


}
