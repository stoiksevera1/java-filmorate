package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    public final UserStorage userStorage;

    public User addFriend(Long id, Long friendId) {
        if (!userStorage.getUsers().contains(userStorage.getUser(friendId))) {
            log.warn("Ошибка поиска пользователя при добавлении друга Пользователь c ID:{} не найден", friendId);
            throw new ValidationException("Пользователь c ID:" + friendId + " не найден");
        }
        if (!(userStorage.getUser(id).getFriends() == null) && userStorage.getUser(id).getFriends().contains(friendId)) {
            log.warn("Ошибка  при добавлении друга Пользователь c ID:{} уже в друзьях", friendId);
            throw new ValidationException("Пользователь c ID: " + friendId + " уже в друзьях");
        }
        userStorage.getUser(id).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(id);
        log.trace("Пользователь {} добавлен в друзья", friendId);
        return userStorage.getUser(id);

    }

    public User dellFriend(Long id, Long friendId) {
        if (!userStorage.getUsers().contains(userStorage.getUser(friendId))) {
            log.warn("Ошибка поиска пользователя при удалении друга пользователь c ID;{} не найден", friendId);
        }
        if (!userStorage.getUser(id).getFriends().contains(friendId)) {
            log.warn("Ошибка  при удалении друга Пользователь c ID:{} нет в друзьях", friendId);

        }
        userStorage.getUser(id).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(id);
        log.trace("Пользователь {} удален из друзей", friendId);
        return userStorage.getUser(id);
    }

    public Collection<User> getFriendsUser(Long id) {
        if (userStorage.getUser(id).getFriends() == null) {
            log.warn("Список друзей пользователя пуст");
            throw new NullPointerException("Список друзей пользователя пуст");
        }
        log.trace("Сформирован cпиcок дhузей пользователя c ID :{}", id);
        return userStorage.getUser(id).getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toSet());

    }

    public Collection<User> getListOfMutualFriends(Long id, Long otherId) {
        if (userStorage.getUser(id).getFriends() == null) {
            log.warn("Список друзей пользователя c id :{} пуст", id);
            throw new NotFoundException("Список друзей пользователя c id :" + id + " пуст");
        }
        if (userStorage.getUser(id).getFriends().stream()
                .noneMatch(idFriends -> userStorage.getUser(otherId).getFriends().contains(idFriends))) {
            log.warn("Нет общих друзей");
            throw new ValidationException("Нет общих друзей");
        }
        log.trace("Выведен список общих друзей");
        return userStorage.getUser(id).getFriends().stream()
                .filter(idFriends -> userStorage.getUser(otherId).getFriends().contains(idFriends))
                .map(userId -> userStorage.getUser(userId))
                .collect(Collectors.toSet());
    }
}

