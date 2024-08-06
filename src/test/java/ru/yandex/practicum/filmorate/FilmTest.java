package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    @Test
    void CreateCorrectFilm() {
        FilmController filmController = new FilmController();
        Film film = new Film();

        film.setName("Жара");
        film.setDescription("Очень сильная жара");
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
        film.setDescription("Очень сильная жара");
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

        film.setName("Жара");
        film.setDescription("Очень, очень, очень, очень, очень сильная жара и " +
                "чоьстцйущвойтчойчдл   ийчйь   тч рчрцч ыч ч йцусотйцуойтцуйоцты" +
                "ловтчйоцутчоцутсойцутсжйдлтч шуцчйущчшйоц шоцушчщшошщоч ш  оуцщшчощз2шчошщ" +
                "щучщ2ущйцтсщзйцтсзойцутст  шщ2усзщ 2утст   учот    ойытч   твзщ    я");
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

        film.setName("Жара");
        film.setDescription("Очень сильная жара");
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

        film.setName("Жара");
        film.setDescription("Очень сильная жара");
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
        film.setName("Жара");
        film.setDescription("Очень сильная жара");
        film.setReleaseDate(LocalDate.of(1995, 1, 12));
        film.setDuration(120);

        Film film2 = new Film();
        film2.setName("Жара");
        film2.setDescription("Очень сильная жара");
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
        film.setName("Жара");
        film.setDescription("Очень сильная жара");
        film.setReleaseDate(LocalDate.of(1995, 1, 12));
        film.setDuration(120);

        Film film2 = new Film();
        film2.setName("Жара");
        film2.setDescription("Очень сильная жара");
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
        film.setName("Жара");
        film.setDescription("Очень сильная жара");
        film.setReleaseDate(LocalDate.of(1995, 1, 12));
        film.setDuration(120);
        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.update(film);
        });
    }

}
