package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public interface UserStorage {

    List<User> getAllUsers();

    User getUser(long id);

    User create(User user);

    void deleteUser(long id);

    User update(User user);

    List<Long> getUserHashKey();

}