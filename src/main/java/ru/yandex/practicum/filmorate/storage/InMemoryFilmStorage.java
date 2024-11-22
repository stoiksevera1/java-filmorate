package ru.yandex.practicum.filmorate.storage;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getAllFilm() {
        log.info("Получение списка фильмов.");
        return films.values();
    }

    public Film addFilm(Film film) {

        if (checkDate(film.getReleaseDate())) {
            log.warn("Ошибка валидации по времени при добовление фильма");
            throw new ValidationException("Раньше даты первого фильма");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Успешнон добавление фильма");
        return film;
    }

    public Film updateFilm(Film newFilm) {

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

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean checkDate(LocalDate data) {
        return data.isBefore(LocalDate.of(1895, 12, 28));
    }


}
