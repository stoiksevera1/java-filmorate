package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.ArrayList;
import java.util.stream.Collectors;

public class FilmMappers {


    public static FilmDto toDto(Film model) {
        return FilmDto.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .releaseDate(model.getReleaseDate())
                .duration(model.getDuration())
                .mpa(model.getRating() != null ? RatingMappers.toDto(model.getRating()) : null)
                .genres(model.getGenres() != null ? model.getGenres().stream()
                        .map(GenreMappers::toDto)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .likes(model.getLikes())
                .build();
    }

    public static Film toModel(FilmDto dto) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .rating(dto.getMpa() != null ? RatingMappers.toModel(dto.getMpa()) : null)
                .genres(dto.getGenres() != null ? dto.getGenres().stream()
                        .map(GenreMappers::toModel)
                        .collect(Collectors.toList()) : new ArrayList<>())

                .build();
    }

}
