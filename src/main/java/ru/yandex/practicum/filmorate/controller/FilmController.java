package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j

@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получение списка фильмов.");
        return films.values();
    }


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {

        if (checkDate(film.getReleaseDate())) {
            log.warn("Ошибка валидации по времени при добовление фильма");
            throw new ValidationException("Раньше даты первого фильма");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Успешнон добавление фильма");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {

        if (!films.containsKey(newFilm.getId())) {
            log.warn("Не найден по id");
            throw new ValidationException("Фильм " + newFilm.getName() + "не найден");
        }

        if (checkDate(newFilm.getReleaseDate())) {
            log.warn("Ошибка валидации по времени при обновлении фильма");
            throw new ValidationException("Раньше даты первого фильма");

        }
        films.put(newFilm.getId(), newFilm);
        log.info("Успешнон обновление фильма");
        return newFilm;
    }


    private boolean checkDate(LocalDate data) {
        return data.isBefore(LocalDate.of(1895, 12, 28));
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
