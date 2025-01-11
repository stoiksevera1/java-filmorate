package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UserDto {

    Long id;
    @NotBlank(message = "email Не может быть пустым")
    @Email(message = "Неправильно введен email")
    String email;
    @NotNull(message = "Логин не может быть пустым")
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^ ]*$", message = "логин не может содержать пробелы")
    String login;
    String name;
    @NotNull(message = " Не заполнена дата")
    @Past(message = "дата рождения не может быть в будущем.")
    LocalDate birthday;
}
