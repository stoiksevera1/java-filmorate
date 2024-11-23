package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    UserStorage userStorage;

    public User addFriend(Long id, Long friendId) {
        if (!userStorage.getUsers().contains(userStorage.getUser(friendId))) {
            log.warn("Ошибка поиска пользователя при добавлении друга Пользователь c ID:{} не найден", friendId);
            throw new ValidationException("Пользователь c ID:" + friendId + " не найден");
        }
        if (userStorage.getUser(id).getFriends().contains(friendId)) {
            log.warn("Ошибка  при добавлении друга Пользователь c ID:{} уже в друзьях", friendId);
            throw new ValidationException("Пользователь c ID: " + friendId + " уже в друзьях");
        }
            userStorage.getUser(id).getFriends().add(friendId);
            userStorage.getUser(friendId).getFriends().add(id);
        log.trace("Пользователь {} добавлен", friendId);
        return userStorage.getUser(id);

    }

    public  User dellFriend(Long id, Long friendId) {
        if (!userStorage.getUsers().contains(userStorage.getUser(friendId))) {
            log.warn("Ошибка поиска пользователя при удалении друга пользователь c ID;{} не найден", friendId);
            throw new ValidationException("Пользователь с ID" + friendId + " не найден");
        }
        if (!userStorage.getUser(id).getFriends().contains(friendId)) {
            log.warn("Ошибка  при удалении друга Пользователь c ID:{} нет в друзьях", friendId);
            throw new ValidationException("Пользователь c ID: " + friendId + " нет в друзьях");
        }
        userStorage.getUser(id).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(id);
        log.trace("Пользователь {} удален", friendId);
        return userStorage.getUser(id);
    }
}
