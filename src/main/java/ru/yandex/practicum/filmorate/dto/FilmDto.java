package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class FilmDto {

    final List<Long> likes;
    final List<GenreDto> genres;

    RatingDto mpa;

    Long id;

    @NotNull
    @NotEmpty
    String name;

    @NotNull
    @Pattern(regexp = "^(?=.{1,200}$).*")
    String description;

    @NotNull
    LocalDate releaseDate;

    @NotNull
    @Positive
    Long duration;

}
