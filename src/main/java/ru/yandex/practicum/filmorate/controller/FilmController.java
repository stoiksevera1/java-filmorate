package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.util.Collection;


@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> findAll() {
        return filmService.getAllFilm();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable("id") Long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public FilmDto createFilm(@Valid @RequestBody FilmDto filmDto) {
        return filmService.addFilm(filmDto);
    }

    @PutMapping
    public FilmDto updateFilm(@Valid @RequestBody FilmDto filmDto) {
        return filmService.updateFilm(filmDto);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmDto addLikeUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return filmService.addLikeUser(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmDto dellLikeUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return filmService.dellLikeUser(id, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getListFilmsPopular(@RequestParam(value = "count", defaultValue = "10") Long count) {
        return filmService.getListFilmsPopular(count);
    }

}
