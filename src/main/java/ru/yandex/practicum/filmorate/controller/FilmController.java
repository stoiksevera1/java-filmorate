package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.util.Collection;


@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.filmStorage.getAllFilm();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmService.filmStorage.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLikeUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return filmService.addLikeUser(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film dellLikeUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return filmService.dellLikeUser(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getListFilmsPopular(@RequestParam(value = "count", defaultValue = "10") Long count) {
        return filmService.getListFilmsPopular(count);
    }

}
