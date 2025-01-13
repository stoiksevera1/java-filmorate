package ru.yandex.practicum.filmorate.storage;


import java.util.Collection;

public interface FriendshipUser {
    public void addFriend(Long id, Long friendId, String status);

    public void dellFriend(Long id, Long friendId);

    public Collection<Long> getFriendsUser(Long id);

    public boolean checkFriendship(Long friendId, Long id);
}
