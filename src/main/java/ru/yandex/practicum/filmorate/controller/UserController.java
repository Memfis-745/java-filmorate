package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.WorkInterface;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@RestController
@Getter
public class UserController {
    private final FilmService filmService;
    private final UserService userService;
    private final Map<Long, User> users = new HashMap<>();
    private static final String pathForFriends = "/{id}/friends/{" +
            "friendId}";

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        log.info("Поиск друзей пользователя {}", id);
        return userService.getUserFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Поиск общих друзей пользователя {} и пользователю {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @Validated(WorkInterface.Create.class)
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Начало обработки запроса по добавлению нового пользователя {}", user);
        return userService.create(user);
    }

    @DeleteMapping(value = "/{id}")
    public User delete(@PathVariable Long id) {
        log.info("Удаление юзера {}", id);
        return userService.delete(id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Запрос на изменение пользователя на {}", newUser);
        return userService.update(newUser);
    }

    @PutMapping(value = pathForFriends)
    public List<User> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = pathForFriends)
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Удаление друга {} у пользователя {}", friendId, id);
        userService.removeFriend(id, friendId);
    }

}
