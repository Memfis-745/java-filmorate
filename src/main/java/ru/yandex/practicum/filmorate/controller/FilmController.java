package ru.yandex.practicum.filmorate.controller;

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
    public Film create(@RequestBody Film film) {
        // проверяем выполнение необходимых условий
        log.info("Начало обработки запроса по добавлению фильма {}", film);
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ConditionsNotMetException("Описание больше 200 знаков");
        }
        if (film.getDuration() <= 0) {
            throw new ConditionsNotMetException("Длительность должна быть положительным числом");
        }

        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ConditionsNotMetException("Дата не может быть раньше 28.12.1895");
        }
        // формируем дополнительные данные.setId(getNextId());
        film.setId(getNextId());
        // сохраняем новую публикацию в памяти приложения
        films.put(film.getId(), film);

        log.info("Конец обработки запроса по добавлению фильма {} и его id {}", film, film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Начало обработки запроса по обновлению фильма: {} и его id {}", newFilm, newFilm.getId());
        // проверяем необходимые условия
        // System.out.println(newPost.getId());
        if (newFilm.getId() == 0) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film film = films.get(newFilm.getId());
            log.info("Старый фильм {} и его id {}", film, film.getId());

            if (!(newFilm.getName() == null || newFilm.getName().isBlank())) {
                film.setName(newFilm.getName());
            }
            if (film.getDescription().length() > 200) {
                throw new ConditionsNotMetException("Описание больше 200 знаков");
            } else {
                film.setDescription(newFilm.getDescription());
            }
            if (film.getDuration() <= 0) {
                throw new ConditionsNotMetException("Длительность должна быть положительным числом");
            } else {
                film.setDuration(newFilm.getDuration());
            }
            if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
                throw new ConditionsNotMetException("Дата не может быть раньше 28.12.1895");
            } else {
                film.setReleaseDate(newFilm.getReleaseDate());
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
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
}
