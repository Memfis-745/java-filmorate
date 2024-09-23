package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getAllUsers();

    Optional<User> getUser(Long id);

    void create(User user);

    void update(User user);

    void addFriends(Long id, Long friendId);

    void removeFriends(Long id, Long friendId);

    List<User> findAllFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);

    void isUserNotExists(Long id);
}