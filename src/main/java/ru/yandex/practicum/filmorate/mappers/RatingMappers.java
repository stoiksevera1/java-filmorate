package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.model.Rating;


public class RatingMappers {
    public static RatingDto toDto(Rating model) {
        return RatingDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    public static Rating toModel(RatingDto dto) {
        return Rating.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
