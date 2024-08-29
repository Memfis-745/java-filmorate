package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final String pathForLike = "/{id}/like/{userId}";
    private final FilmService filmService;


    @GetMapping
    public List<Film> findAll() {
        log.info("Поиск всех фильмов по полулярности");
        return filmService.getAllFilms();
    }

    @GetMapping(value = "/{id}")
    public Film findFilm(@PathVariable long id) {
        log.info("Поиск фильмов по id {}", id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(required = false) Integer count) {
        log.info("Список {} фильмов по полулярности", count);

        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Начало обработки запроса по добавлению фильма {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) throws ValidationException {
        log.info("Начало обработки запроса по обновлению фильма: {} и его id {}", newFilm, newFilm.getId());

        return filmService.updateFilm(newFilm);

    }

    @PutMapping(value = pathForLike)
    public void addLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление лайка фильму: {} пользователем {}", id, userId);

        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = pathForLike)
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление лайка фильму: {} пользователем {}", id, userId);

        filmService.deleteLike(id, userId);
    }

}
