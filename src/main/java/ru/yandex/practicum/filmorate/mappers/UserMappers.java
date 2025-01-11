package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;


public class UserMappers {
    public static UserDto toDto(User model) {
        return UserDto.builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .login(model.getLogin())
                .birthday(model.getBirthday())
                .build();
    }

    public static User toModel(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .birthday(dto.getBirthday())
                .build();
    }
}
