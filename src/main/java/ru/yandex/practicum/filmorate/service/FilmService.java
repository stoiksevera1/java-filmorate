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
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    public Film addLikeUser(Long id, Long userId) {
        if (userStorage.getUser(userId) != null)
            filmStorage.getFilmById(id).getLikes().add(userId);
        return filmStorage.getFilmById(id);
    }

    public Film dellLikeUser(Long id, Long userId) {
        if (userStorage.getUser(userId) != null)
            filmStorage.getFilmById(id).getLikes().remove(userId);
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> getListFilmsPopular(Long count) {
        return filmStorage.getAllFilm().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toSet());
    }
}
