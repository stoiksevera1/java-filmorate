package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;


import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {

        User user = User.builder()
                .name("dfsd")
                .email("dfsd@dsfdds.ru")
                .login("fddsf")
                .birthday(LocalDate.of(1991, 12, 21))
                .build();
        userStorage.add(user);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
}