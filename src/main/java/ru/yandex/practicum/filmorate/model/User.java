package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.time.LocalDate;

@EqualsAndHashCode(of = {"id"})
@Data
public class User {

    Long id;

    String name;

    @NotBlank(message = "email Не может быть пустым")
    @Email(message = "Неправильно введен email")
    String email;

    @NotNull(message = "Логин не может быть пустым")
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^ ]*$", message = "логин не может содержать пробелы")
    String login;

    @NotNull(message = " Не заполнена дата")
    @Past(message = "дата рождения не может быть в будущем.")
    LocalDate birthday;

}
