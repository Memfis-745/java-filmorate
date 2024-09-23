package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;


import org.springframework.beans.factory.annotation.Autowired;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserTest {
    private static Validator validator;
    private static UserController userController;

    static {

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.usingContext().getValidator();

    }

    @Test
    void createCorrectUser() {

        User user = new User();
        user.setEmail("email@mail.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Ошибка валидации при создании объекта.");
        assertEquals(user.getLogin(), "login", "Некорректный логин.");
        assertEquals(user.getName(), "name", "Неправильно задано имя.");
        assertEquals(user.getBirthday(), LocalDate.of(1990, 1, 12),
                "Неверно задана дата рождения.");

    }


    @Test
    void createUserWithoutEmail() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        User user = new User();
        user.setEmail("");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createUserIncorrectEmail() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        User user = new User();
        user.setEmail("kolya_vasya.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createUserWithoutLogin() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
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
        User user = new User();
        user.setEmail("kolya@vasya.ru");
        user.setLogin("kolya");
        user.setName("name");
        user.setBirthday(LocalDate.of(2025, 1, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
