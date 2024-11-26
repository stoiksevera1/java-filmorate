package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        log.info("Получение списка фильмов FilmStorage.");
        return films.values();
    }

    public Film addFilm(Film film) {
        if (checkDate(film.getReleaseDate())) {
            log.warn("Ошибка валидации по времени при добовление фильма");
            throw new ValidationException("Раньше даты первого фильма");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Успешнон добавление фильма FilmStorage.");
        return film;
    }

    public Film updateFilm(Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            log.warn("Фильм по ID :" + newFilm.getId() + "не найден");
            throw new NotFoundException("Фильм по ID :" + newFilm.getId() + "не найден");
        }
        if (checkDate(newFilm.getReleaseDate())) {
            log.warn("Ошибка валидации по времени при обновлении фильма");
            throw new ValidationException("Раньше даты первого фильма");
        }
        films.put(newFilm.getId(), newFilm);
        log.info("Успешнон обновление фильма FilmStorage.");
        return newFilm;
    }

    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм по ID : " + id + " найден");
        }
        return films.get(id);
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
