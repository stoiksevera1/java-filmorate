package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"id"})
public class Film {

    Long id;

    final Set<Long> Likes = new HashSet<>();

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
