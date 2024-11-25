package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public Collection<Film> getAllFilm();

    public Film getFilmById(Long id);
}
