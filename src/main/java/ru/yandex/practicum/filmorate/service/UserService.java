package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserMappers;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipUser;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipUser friendshipUser;

    public UserDto addFriend(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        userStorage.getUser(friendId);
        if (friendshipUser.checkFriendship(friendId, id)) friendshipUser.addFriend(id, friendId, "Подтвержденная");
        else friendshipUser.addFriend(id, friendId, "Неподтвержденная");
        log.trace("Пользователь {} добавлен в друзья", friendId);

        return UserMappers.toDto(user);

    }

    public UserDto dellFriend(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        userStorage.getUser(friendId);
        friendshipUser.dellFriend(id, friendId);
        log.trace("Пользователь {} удален из друзей", friendId);
        return UserMappers.toDto(user);
    }

    public Collection<UserDto> getFriendsUser(Long id) {
        userStorage.getUser(id);
        log.trace("Сформирован cпиcок друзей пользователя c ID :{}", id);
        return friendshipUser.getFriendsUser(id).stream()
                .map(userStorage::getUser)
                .map(UserMappers::toDto)
                .collect(Collectors.toSet());
    }

    public Collection<UserDto> getListOfMutualFriends(Long id, Long otherId) {
        userStorage.getUser(id);
        userStorage.getUser(otherId);
        Collection<Long> friendsListUser1 = friendshipUser.getFriendsUser(id);
        Collection<Long> friendsListUser2 = friendshipUser.getFriendsUser(otherId);
        if (friendsListUser1.isEmpty()) {
            log.warn("Список друзей пользователя c id :{} пуст", id);
            throw new NotFoundException("Список друзей пользователя c id :" + id + " пуст");
        }
        if (friendsListUser1.stream().noneMatch(friendsListUser2::contains)) {
            log.warn("Нет общих друзей");
            throw new ValidationException("Нет общих друзей");
        }
        log.trace("Выведен список общих друзей");
        return friendsListUser1.stream()
                .filter(friendsListUser2::contains)
                .map(userStorage::getUser)
                .map(UserMappers::toDto)
                .collect(Collectors.toSet());
    }

    public UserDto createUser(UserDto userDto) {
        User user = UserMappers.toModel(userDto);
        if (checkName(user)) {
            user.setName(user.getLogin());
        }

        return UserMappers.toDto(userStorage.add(user));
    }

    public UserDto getUser(Long userId) {
        return UserMappers.toDto(userStorage.getUser(userId));
    }

    public Collection<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMappers::toDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(UserDto newUser) {
        User user = UserMappers.toModel(newUser);
        return UserMappers.toDto(userStorage.update(user));
    }

    private boolean checkName(User user) {
        return user.getName() == null || user.getName().isEmpty();
    }
}

