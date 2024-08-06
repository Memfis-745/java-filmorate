package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    @Test
    void CreateCorrectFilm() {
        FilmController filmController = new FilmController();
        Film film = new Film();

        film.setName("Jara");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1990, 1, 12));
        film.setDuration(120);
        Film filmCont = filmController.create(film);
        assertEquals(film, filmCont, "Переданный и возвращенный классы не совпадают.");
    }

    @Test
    void CreateFilmWithoutName() {
        FilmController filmController = new FilmController();
        Film film = new Film();

        film.setName("");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1990, 1, 12));
        film.setDuration(120);
        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void CreateFilmWithBigDescription() {
        FilmController filmController = new FilmController();
        Film film = new Film();

        film.setName("Jara");
        film.setDescription("   weuc    wuciui  qwpoxunuhe wuocnwjenw sdcwuecn  qkmbu qwecnqwoecu" +
                "WDSCIHOGWEYQW DCKJBWCIUOWE jshcuiercuie celknruiofu3nf  rcqjnrcuwqec" +
                "cbheiyrhwbechervcbr cqjnoeicuqberc qcejqnoiweucheiuc ceocnpowuecqw" +
                "cerbciwervhgeroyvwerb qwlecqjweboyiebfoweib    q qcwcnwcioruvhrtioyvrtv ");
        film.setReleaseDate(LocalDate.of(1990, 1, 12));
        film.setDuration(120);
        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void CreateFilmWithNegativeDuration() {
        FilmController filmController = new FilmController();
        Film film = new Film();

        film.setName("Jara");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1990, 1, 12));
        film.setDuration(-120);
        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void CreateFilmBeforeEraKino() {
        FilmController filmController = new FilmController();
        Film film = new Film();

        film.setName("Jara");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1495, 1, 12));
        film.setDuration(120);
        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void UpdateFilm() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("Jara");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1995, 1, 12));
        film.setDuration(120);

        Film film2 = new Film();
        film2.setName("Jara");
        film2.setDescription("Very strong jara");
        film2.setReleaseDate(LocalDate.of(1995, 1, 12));
        film2.setDuration(120);

        assertEquals(film, filmController.create(film), "Переданный и возвращенный классы не совпадают.");
        film2.setId(film.getId());
        assertEquals(film, filmController.update(film2), "Переданный и возвращенный классы не совпадают.");

    }

    @Test
    void UpdateNotExistFilm() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("Jara");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1995, 1, 12));
        film.setDuration(120);

        Film film2 = new Film();
        film2.setName("jara");
        film2.setDescription("Very strong jara");
        film2.setReleaseDate(LocalDate.of(1995, 1, 12));

        assertEquals(film, filmController.create(film), "Переданный и возвращенный классы не совпадают.");
        film2.setId(100L);
        assertThrows(NotFoundException.class, () -> {
            filmController.update(film2);
        });
    }

    @Test
    void UpdateFilmWithoutId() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("Very");
        film.setDescription("Very strong jara");
        film.setReleaseDate(LocalDate.of(1995, 1, 12));
        film.setDuration(120);
        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.update(film);
        });
    }

}
