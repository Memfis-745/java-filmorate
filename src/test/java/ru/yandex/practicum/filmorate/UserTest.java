package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private Validator validator;

    @Before
    public void init() {

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();

    }

    @Test
    void createCorrectUser() {
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
    void createFilmWithoutName() {

        FilmController filmController = new FilmController();
        Film film = new Film();

        film.setName("");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1990, 1, 12));
        film.setDuration(120);
        Film filmCont = filmController.create(film);

    }

    @Test
    void createUserWithoutEmail() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

    }

    @Test
    void createUserIncorrectEmail() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya_vasya.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createUserWithExistsEmail() {
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
    void createUserWithoutLogin() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya@vasya.ru");
        user.setLogin("");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createUserWithBirthdayInTheFuture() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("kolya@vasya.ru");
        user.setLogin("kolya");
        user.setName("name");
        user.setBirthday(LocalDate.of(2025, 1, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void updateUser() {
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
    void updateNotExistUser() {
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
    void updateUserWithoutId() {
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
