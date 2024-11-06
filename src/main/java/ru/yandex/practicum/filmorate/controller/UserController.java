package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получение списка пользователей.");
        return users.values();
    }


    @PostMapping
    public User createUser(@Valid @RequestBody User user) {

        if (checkName(user)) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Успешное добавление данных пользователя");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
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
