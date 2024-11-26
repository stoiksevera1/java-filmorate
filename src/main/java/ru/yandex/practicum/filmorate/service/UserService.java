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
    private final UserStorage userStorage;

    public User addFriend(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.trace("Пользователь {} добавлен в друзья", friendId);
        return user;

    }

    public User dellFriend(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        log.trace("Пользователь {} удален из друзей", friendId);
        return user;
    }

    public Collection<User> getFriendsUser(Long id) {
        log.trace("Сформирован cпиcок друзей пользователя c ID :{}", id);
        return userStorage.getUser(id).getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toSet());

    }

    public Collection<User> getListOfMutualFriends(Long id, Long otherId) {
        User user = userStorage.getUser(id);
        User otherUser = userStorage.getUser(otherId);
        if (user.getFriends().isEmpty()) {
            log.warn("Список друзей пользователя c id :{} пуст", id);
            throw new NotFoundException("Список друзей пользователя c id :" + id + " пуст");
        }
        if (user.getFriends().stream()
                .noneMatch(idFriends -> otherUser.getFriends().contains(idFriends))) {
            log.warn("Нет общих друзей");
            throw new ValidationException("Нет общих друзей");
        }
        log.trace("Выведен список общих друзей");
        return user.getFriends().stream()
                .filter(idFriends -> otherUser.getFriends().contains(idFriends))
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    public User createUser(User user) {
        return userStorage.add(user);
    }

    public User getUser(Long userId) {
        return userStorage.getUser(userId);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User updateUser(User newUser) {
        return userStorage.update(newUser);
    }
}

