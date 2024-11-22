package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
UserStorage userStorage;

    @GetMapping
    public Collection<User> getListUsers() {
        return userStorage.getUsers();
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable("userId") Long userId){
        return userStorage.getUser(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
    return  userStorage.add(user);

    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
      return   userStorage.update(newUser);
    }




}
