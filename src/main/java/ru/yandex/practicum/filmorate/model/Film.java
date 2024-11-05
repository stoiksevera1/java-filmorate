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

    @Pattern(regexp = "^(?=.{1,200}$).*")
    String description;
    LocalDate releaseDate;
    @Positive
    Long duration;
}
