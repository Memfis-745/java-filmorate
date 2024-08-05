package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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

    // long id; String username; String email; String password; Instant registrationDate;
    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Начало обработки запроса по добавлению нового пользователя {}", user);
        // проверяем выполнение необходимых условий
        log.trace("Проверка е-мейла нового пользователя {}", user.getEmail());
        if (user.getEmail() == null || !(user.getEmail().contains("@"))) {
            throw new ConditionsNotMetException("Е-мейл должен быть корректен");
        }
        for (User i : users.values()) {
            if (i.getEmail().equals(user.getEmail())) {
                throw new DuplicatedDataException("Этот Е-мейл уже используется");
            }
        }
        // формируем дополнительные данные
        user.setId(getNextId());
        log.trace("Добавление id нового пользователя {}", user.getId());
        if ((user.getLogin() == null) || user.getLogin().isBlank()) {
            System.out.println("Логин должен быть указан  " + user.getLogin());
            throw new ConditionsNotMetException("Логин должен быть указан");
        }
        log.trace("Проверка имени нового пользователя {}", user.getName());
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.trace("Проверка даты рождения нового пользователя {}", user.getBirthday());
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionsNotMetException("Дата не может быть в будущем");
        }
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        log.info("Новый пользователь создан {}", user);
        return user;
    }


    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Запрос на изменение пользователя на {}", newUser);

        if (newUser.getId() == 0) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if ((newUser.getEmail() == null) || (!(newUser.getEmail().contains("@")))) {
            throw new ConditionsNotMetException("Е-мейл должен быть корректен");
        }
        if (users.containsKey(newUser.getId())) {

            User user = users.get(newUser.getId());
            log.info("Старый пользователь по этому id {}", user);
            user.setEmail(newUser.getEmail());
            if ((newUser.getLogin() == null) || newUser.getLogin().isBlank()) {
                throw new ConditionsNotMetException("Логин должен быть указан");
            } else {
                user.setLogin(newUser.getLogin());
            }

            if ((newUser.getName() == null) || newUser.getName().isBlank()) {
                user.setName(user.getLogin());
            } else {
                user.setName(newUser.getName());
            }

            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ConditionsNotMetException("Дата не может быть в будущем");
            } else {
                user.setBirthday(newUser.getBirthday());
            }
            log.info("Старый пользователь по этому id {}", user);
            return user;
        } else {
            throw new NotFoundException("Юзер с id = " + newUser.getId() + " не найден");
        }
    }


    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
