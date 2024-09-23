package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;

import jakarta.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.Validator.Create;

import ru.yandex.practicum.filmorate.controller.FilmController;

import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmTest {
    static Validator validator;
    static FilmController filmController;


    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @SneakyThrows

    @Test
    void shouldCreateCorrectFilm() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2030, 1, 10));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertTrue(violations.isEmpty(), "Присутствуют нарушения.");
        assertEquals(film.getDescription(), "description", "Описание было присвоено некорректно.");
        assertEquals(film.getName(), "name", "Имя было присвоено некорректно.");
        assertEquals(film.getReleaseDate(), LocalDate.of(2030, 1, 10),
                "Дата релиза была присвоена некорректно.");
    }


    @Test
    void createFilmWithBigDescription() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();

        Film film = new Film();
        film.setName("Jara");
        film.setDescription("   weuc    wuciui  qwpoxunuhe wuocnwjenw sdcwuecn  qkmbu qwecnqwoecu" +
                "WDSCIHOGWEYQW DCKJBWCIUOWE jshcuiercuie celknruiofu3nf  rcqjnrcuwqec" +
                "cbheiyrhwbechervcbr cqjnoeicuqberc qcejqnoiweucheiuc ceocnpowuecqw" +
                "cerbciwervhgeroyvwerb qwlecqjweboyiebfoweib    q qcwcnwcioruvhrtioyvrtv ");
        film.setReleaseDate(LocalDate.of(1990, 1, 12));
        film.setDuration(120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createFilmWithNegativeDuration() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        Film film = new Film();

        film.setName("Jara");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1990, 1, 12));
        film.setDuration(-120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createFilmBeforeEraKino() {
        Film film = new Film();
        film.setName("Jara");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1495, 1, 12));
        film.setDuration(120);
        assertThrows(NullPointerException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void updateFilmWithoutId() {
        Film film = new Film();
        film.setName("Very");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1995, 1, 12));
        film.setDuration(120);
        assertThrows(NullPointerException.class, () -> {
            filmController.update(film);
        });
    }

}
