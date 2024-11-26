package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addLikeUser(Long id, Long userId) {
        userStorage.getUser(userId);
        log.info("Добавление лайка пользователя.");
        filmStorage.getFilmById(id).getLikes().add(userId);
        return filmStorage.getFilmById(id);
    }

    public Film dellLikeUser(Long id, Long userId) {
        userStorage.getUser(userId);
        log.info("Удаление лайка пользователя.");
        filmStorage.getFilmById(id).getLikes().remove(userId);
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> getListFilmsPopular(Long count) {
        log.info("Получение списка фильмов отсортированных по количеству лайков.");
        return filmStorage.getAllFilm().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toSet());
    }

    public Collection<Film> getAllFilm() {
        return filmStorage.getAllFilm();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }
}
