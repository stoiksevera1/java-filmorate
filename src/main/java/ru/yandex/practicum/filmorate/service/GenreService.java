package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mappers.GenreMappers;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDbStorage genreDbStorage;


    public Collection<GenreDto> getAllGenre() {
        log.info("Получение списка жанров.");
        return genreDbStorage.getAllGenre().stream()
                .map(GenreMappers::toDto)
                .collect(Collectors.toList());
    }


    public GenreDto getGenreById(Long id) {
        log.info("Получение жанра по ID.");
        return GenreMappers.toDto(genreDbStorage.getGenreById(id));
    }

}
