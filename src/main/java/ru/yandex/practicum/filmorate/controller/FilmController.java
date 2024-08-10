package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Начало обработки запроса по добавлению фильма {}", film);

        validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Конец обработки запроса по добавлению фильма {} и его id {}", film, film.getId());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Начало обработки запроса по обновлению фильма: {} и его id {}", newFilm, newFilm.getId());

        if (newFilm.getId() == 0) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film film = films.get(newFilm.getId());
            log.info("Старый фильм {} и его id {}", film, film.getId());

            validate(newFilm);
            film.setName(newFilm.getName());
            film.setDuration(newFilm.getDuration());
            film.setReleaseDate(newFilm.getReleaseDate());
            film.setDescription(newFilm.getDescription());
            log.info("Результат операции по обновлению фильма {} и его id {}", film, film.getId());
            return film;
        } else {
            throw new NotFoundException("Пост с id = " + newFilm.getId() + " не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ConditionsNotMetException("Дата не может быть раньше 28.12.1895");
        }
    }


}

