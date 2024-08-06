package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void CreateCorrectUser() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("email@mail.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));
        User userCont = userController.create(user);
        assertEquals(user, userCont, "Переданный и возвращенный классы не совпадают.");
    }

    @Test
    void CreateUserWithoutEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));
        assertThrows(ConditionsNotMetException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void CreateUserIncorrectEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya_vasya.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));
        assertThrows(ConditionsNotMetException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void CreateUserWithExistsEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya@vasya.ru");
        user.setLogin("petya");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));
        userController.create(user);

        User user2 = new User();
        user2.setEmail("kolya@vasya.ru");
        user2.setLogin("kolya");
        user2.setName("name");
        user2.setBirthday(LocalDate.of(1990, 1, 12));

        assertThrows(DuplicatedDataException.class, () -> {
            userController.create(user2);
        });
    }

    @Test
    void CreateUserWithoutLogin() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya@vasya.ru");
        user.setLogin("");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));
        assertThrows(ConditionsNotMetException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void CreateUserWithBirthdayInTheFuture() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya@vasya.ru");
        user.setLogin("kolya");
        user.setName("name");
        user.setBirthday(LocalDate.of(2025, 1, 12));
        assertThrows(ConditionsNotMetException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void UpdateUser() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya@vasya.ru");
        user.setLogin("petya");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));

        User user2 = new User();
        user2.setEmail("ne-kolya@vasya.ru");
        user2.setLogin("masha");
        user2.setName("olya");
        user2.setBirthday(LocalDate.of(1991, 1, 12));

        assertEquals(user, userController.create(user), "Переданный и возвращенный классы не совпадают.");
        user2.setId(user.getId());
        assertEquals(user2, userController.update(user2), "Переданный и возвращенный классы не совпадают.");

    }

    @Test
    void UpdateNotExistUser() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya@vasya.ru");
        user.setLogin("petya");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));

        User user2 = new User();
        user2.setEmail("ne-kolya@vasya.ru");
        user2.setLogin("masha");
        user2.setName("olya");
        user2.setBirthday(LocalDate.of(1991, 1, 12));

        assertEquals(user, userController.create(user), "Переданный и возвращенный классы не совпадают.");
        user2.setId(100L);
        assertThrows(NotFoundException.class, () -> {
            userController.update(user2);
        });
    }

    @Test
    void UpdateUserWithoutId() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya_vasya.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));
        assertThrows(ConditionsNotMetException.class, () -> {
            userController.update(user);
        });
    }

}
