package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    public User add(User user);

    public User update(User user);

    public Collection<User> getUsers();

    public User getUser(Long id);
}
