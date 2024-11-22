package ru.yandex.practicum.filmorate.storage;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public User getUser(Long id){
        return users.get(id);
    }

    public Collection<User> getUsers() {
        log.info("Получение списка пользователей.");
        return users.values();
    }

    public User add(User user) {
    if (checkName(user)) {
        user.setName(user.getLogin());
    }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Успешное добавление данных пользователя");
        return user;
}

    public User update(User newUser) {
        if (!users.containsKey(newUser.getId())) {
            log.warn("Ошибка поиска пользователя ID не найден");
            throw new ValidationException("Пользователь " + newUser.getId() + " не найден");
        }
        if (checkName(newUser)) {
            newUser.setName(newUser.getLogin());
        }
        users.put(newUser.getId(), newUser);
        log.info("Успешное обновление данных пользователя");
        return newUser;
    }

    public Collection<User> findAll() {
        log.info("Получение списка пользователей.");
        return users.values();
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
