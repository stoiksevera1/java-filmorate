package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.RatingDto;

import ru.yandex.practicum.filmorate.mappers.RatingMappers;
import ru.yandex.practicum.filmorate.storage.RatingDbStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {


    private final RatingDbStorage ratingDbStorage;


    public Collection<RatingDto> getAllRatings() {
        return ratingDbStorage.getAllRating().stream()
                .map(RatingMappers::toDto)
                .collect(Collectors.toList());
    }


    public RatingDto getRatingById(Long id) {
        return RatingMappers.toDto(ratingDbStorage.getRatingById(id));
    }
}
