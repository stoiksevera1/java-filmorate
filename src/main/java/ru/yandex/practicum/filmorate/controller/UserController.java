package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.Collection;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getListUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") Long userId) {
        return userService.getUser(userId);
    }


    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UserDto newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public UserDto addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {

        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public UserDto dellFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        return userService.dellFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> getFriendsUser(@PathVariable("id") Long id) {
        return userService.getFriendsUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> getListOfMutualFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        return userService.getListOfMutualFriends(id, otherId);
    }


}
