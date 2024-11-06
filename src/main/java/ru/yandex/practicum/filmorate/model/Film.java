package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id"})
public class Film {

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
