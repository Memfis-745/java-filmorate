package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Начало обработки запроса по добавлению нового пользователя {}", user);
        validate(user);
        for (User i : users.values()) {
            if (i.getEmail().equals(user.getEmail())) {
                throw new DuplicatedDataException("Этот Е-мейл уже используется");
            }
        }
        user.setId(getNextId());
        log.trace("Добавление id нового пользователя {}", user.getId());
        log.trace("Проверка имени нового пользователя {}", user.getName());

        users.put(user.getId(), user);
        log.info("Новый пользователь создан {}", user);
        return user;
    }


    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Запрос на изменение пользователя на {}", newUser);
        if (newUser.getId() == 0) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            validate(newUser);
            User user = users.get(newUser.getId());
            log.info("Старый пользователь по этому id {}", user);

            user.setEmail(newUser.getEmail());
            user.setLogin(newUser.getLogin());
            user.setName(newUser.getName());

            user.setBirthday(newUser.getBirthday());


            log.info("Старый пользователь по этому id {}", user);
            return user;
        } else {
            throw new NotFoundException("Юзер с id = " + newUser.getId() + " не найден");
        }
    }


    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validate(User user) {
        log.trace("Проверка имени нового пользователя {}", user.getName());
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }


    }
}
