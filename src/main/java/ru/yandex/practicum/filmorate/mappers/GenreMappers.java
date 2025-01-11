package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;


public class GenreMappers {
    public static GenreDto toDto(Genre model) {
        return GenreDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    public static Genre toModel(GenreDto dto) {
        return Genre.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
