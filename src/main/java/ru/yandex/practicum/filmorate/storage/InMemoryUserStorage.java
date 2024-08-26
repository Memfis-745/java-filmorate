package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    @Getter
    HashMap<Long, User> userHashMap = new HashMap<>();
    long userId = 1;

    @Override
    public User create(User user) {
        user.setId(userId++);
        userHashMap.put(user.getId(), user);
        log.info("Пользователь успешно создан {} c id {}", user, user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        for (User user : userHashMap.values()) {
            log.info("Получение всех пользователей {}", user);
        }
        return new ArrayList<>(userHashMap.values());
    }

    @Override
    public User getUser(long id) {
        return userHashMap.get(id);
    }

    @Override
    public void deleteUser(long id) {
        if (!userHashMap.containsKey(id)) {
            userHashMap.remove(id);
        } else {
            System.out.println(String.format("Пользователь с id %s не найден", id));
        }
    }

    @Override
    public User update(User user) {
        userHashMap.put(user.getId(), user);
        return user;
    }
}