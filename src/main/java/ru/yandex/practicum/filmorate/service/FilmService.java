package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMappers;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmDto addLikeUser(Long id, Long userId) {
        userStorage.getUser(userId);
        filmStorage.getFilmById(id);
        log.info("Добавление лайка пользователя.");
        filmStorage.addLikeUser(id, userId);
        return FilmMappers.toDto(filmStorage.getFilmById(id));
    }

    public FilmDto dellLikeUser(Long id, Long userId) {
        userStorage.getUser(userId);
        log.info("Удаление лайка пользователя.");
        filmStorage.dellLikeUser(id, userId);
        return FilmMappers.toDto(filmStorage.getFilmById(id));
    }

    Comparator<Film> explicitComparator = (film1, film2) -> film1.getLikes().size() - film2.getLikes().size();

    public List<FilmDto> getListFilmsPopular(Long count) {
        log.info("Получение списка фильмов отсортированных по количеству лайков.");
        return filmStorage.getAllFilm().stream()
                .sorted(explicitComparator.reversed())
                .limit(count)
                .map(FilmMappers::toDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getAllFilm() {
        return filmStorage.getAllFilm().stream()
                .map(FilmMappers::toDto)
                .collect(Collectors.toList());
    }

    public FilmDto addFilm(FilmDto filmDto) {
        Film film = FilmMappers.toModel(filmDto);
        if (checkDate(film.getReleaseDate())) {
            log.warn("Ошибка валидации по времени при добовление фильма");
            throw new ValidationException("Раньше даты первого фильма");
        }
        return FilmMappers.toDto(filmStorage.addFilm(film));
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        Film film = FilmMappers.toModel(filmDto);
        return FilmMappers.toDto(filmStorage.updateFilm(film));
    }

    public FilmDto getFilmById(Long id) {
        return FilmMappers.toDto(filmStorage.getFilmById(id));
    }

    private boolean checkDate(LocalDate data) {
        return data.isBefore(LocalDate.of(1895, 12, 28));
    }
}
