package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;


import java.util.*;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Validation validation;


    public List<User> getAllUsers() {
        log.info("Получение всех пользователей getALLUsers");
        return userRepository.getAllUsers();
    }

    public User getUser(long id) {
        log.info("Получение пользователя по id {}", id);
        validation.validId(id);
        return userRepository.getUser(id).orElseThrow(() -> new NotFoundException(
                "Пользователь c ID - " + id + " не найден"));
    }

    public List<User> getUserFriends(long id) {

        validation.validId(id);
        validation.validUser(id);
        return userRepository.findAllFriends(id);
    }


    public List<User> getCommonFriends(Long userId, Long friendId) {

        validation.validId(userId);
        validation.validId(friendId);
        validation.validUser(userId);
        validation.validUser(friendId);

        return userRepository.getCommonFriends(userId, friendId);
    }

    public User create(User user) {
        log.info("Создание пользователя {}", user);
        for (User u : getAllUsers()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new DuplicatedDataException("Пользователь с таким е-мейлом, уже существует");
            }
        }
        userRepository.create(user);
        validation.validUserId(user);
        log.info("Пользователь создан {}", user);
        return user;
    }

    public User delete(Long id) {

        validation.validId(id);
        validation.validUser(id);
        return null;
    }

    public User update(User user) {
        log.info("Обновление данных пользователя с id = {} ", user.getId());
        validation.userName(user);
        validation.validUserId(user);
        log.info("Пользователь существует");
        userRepository.update(user);
        log.info("Пользователь с id = {} обновлен", user.getId());
        return user;

    }

    public List<User> addFriend(long id, long friendId) {

        validation.validId(id);
        validation.validId(friendId);

        validation.validUser(id);
        validation.validUser(friendId);

        userRepository.addFriends(id, friendId);
        log.info("Добавление пользователя {} в друзья к {}", id, friendId);

        return null;
    }

    public void removeFriend(long id, long friendId) {
        validation.validId(id);
        validation.validId(friendId);

        validation.validUser(id);
        validation.validUser(friendId);

        userRepository.removeFriends(id, friendId);
        log.info("У пользователя {} удален друг  {}", id, friendId);
        log.info("У пользователя {} удален друг  {}", friendId, id);
    }
}