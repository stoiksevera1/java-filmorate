package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingDto {
    Long id;
    String name;
}
