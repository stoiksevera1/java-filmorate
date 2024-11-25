package ru.yandex.practicum.filmorate.storage;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            log.warn("Ошибка поиска пользователя ID не найден");
            throw new NotFoundException("Пользователь " + id + " не найден");
        }
        return users.get(id);
    }

    public Collection<User> getUsers() {
        if (users.isEmpty()) {
            throw new NotFoundException("Список пуст");
        }
        log.info("Получение списка пользователей.");
        return users.values();
    }

    public User add(User user) {
        if (checkName(user)) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.warn("Успешное добавление данных пользователя");
        return user;
    }

    public User update(User newUser) {
        if (!users.containsKey(newUser.getId())) {
            log.warn("Ошибка поиска пользователя ID не найден");
            throw new NotFoundException("Пользователь " + newUser.getId() + " не найден");
        }
        if (checkName(newUser)) {
            newUser.setName(newUser.getLogin());
        }
        users.put(newUser.getId(), newUser);
        log.info("Успешное обновление данных пользователя");
        return newUser;
    }


    private boolean checkName(User user) {
        return user.getName() == null || user.getName().isEmpty();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
