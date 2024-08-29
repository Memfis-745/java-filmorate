package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final Validation validation;


    public List<User> getAllUsers() {
        log.info("Получение всех пользователей getALLUsers");
        return userStorage.getAllUsers();
    }

    public User getUser(long id) {
        log.info("Получение пользователя по id {}", id);
        validation.validId(id);
        return userStorage.getUser(id);
    }

    public List<User> getUserFriends(long id) {

        validation.validId(id);
        validation.validUser(id);

        if (getUser(id).getFriends() == null) {
            log.info("У пользователя {} нет друзей", id);
            return new ArrayList<>();
        } else {
            List<User> friends = new ArrayList<>();
            for (long idUser : getUser(id).getFriends()) {
                friends.add(getUser(idUser));
            }
            return friends;
        }
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {

        validation.validId(userId);
        validation.validId(friendId);
        validation.validUser(userId);
        validation.validUser(friendId);
        validation.validUserFriends(userId);
        validation.validUserFriends(friendId);

        User user = getUser(userId);
        User friend = getUser(friendId);

        List<Long> commonFriends = new ArrayList<>();
        commonFriends = user.getFriends().stream()
                .filter(id -> friend.getFriends().contains(id))
                .collect(Collectors.toList());

        List<User> users = new ArrayList<>();
        for (long idUser : commonFriends) {
            users.add(getUser(idUser));
        }
        log.info("Общие друзья y {} и {} : {}", user, friend, users);
        return users;
    }

    public User create(User user) {
        validation.userName(user);
        for (User u : getAllUsers()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new DuplicatedDataException("Пользователь с таким е-мейлом, уже существует");
            }
        }
        return userStorage.create(user);
    }

    public User delete(Long id) {

        validation.validId(id);
        validation.validUser(id);
        userStorage.deleteUser(id);
        return null;
    }

    public User update(User user) {

        validation.userName(user);
        validation.validUser(user.getId());
        return userStorage.update(user);

    }

    public List<User> addFriend(long id, long friendId) {

        validation.validId(id);
        validation.validId(friendId);

        validation.validUser(id);
        validation.validUser(friendId);

        User user = getUser(id);
        User userFriend = getUser(friendId);
        log.info("Добавление пользователя {} в друзья к {}", userFriend, user);

        if (user.getId() == userFriend.getId()) {
            log.info("id пользователя {} и его друга совпадают {}", user.getId(), userFriend.getId());
            throw new DuplicatedDataException("Пользователь, уже в друзьях");
        }
        try {
            addFriends(user, userFriend.getId());
            addFriends(userFriend, user.getId());
            log.info("Данные из хранилища: Юзер {} и френд {}", getUser(id), getUser(friendId));
        } catch (DuplicatedDataException e) {
            throw new DuplicatedDataException("Пользователь, уже в друзьях");
        }

        return null;
    }

    public void removeFriend(long id, long friendId) {
        validation.validId(id);
        validation.validId(friendId);

        validation.validUser(id);
        validation.validUser(friendId);

        User user = getUser(id);
        User friend = getUser(friendId);

        if ((user.getFriends() != null) & (friend.getFriends() != null)) {
            if (user.getFriends().contains(friendId)) {
                user.getFriends().remove(friendId);
                friend.getFriends().remove(id);
                log.info("У пользователя {} удален друг  {}", user, friend);
                log.info("У пользователя {} удален друг  {}", friend, user);
            }
        }
    }

    public User addFriends(User user, Long id) {
        if (user.getFriends() == null) {
            Set<Long> friend = new HashSet<>();
            friend.add(id);
            user.setFriends(friend);
        } else {
            user.getFriends().add(id);
        }
        return user;
    }
}