package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Builder
@Data
@EqualsAndHashCode(of = {"id"})
public class Film {

    List<Long> likes;
    List<Genre> genres;

    Rating rating;

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
