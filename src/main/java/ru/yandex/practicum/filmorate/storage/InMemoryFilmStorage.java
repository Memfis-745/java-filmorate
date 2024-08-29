package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Getter
    HashMap<Long, Film> filmHashMap = new HashMap<>();
    long filmId = 1;

    @Override
    public List<Film> showAllFilms() {
        return new ArrayList<>(filmHashMap.values());
    }

    @Override
    public Film createFilm(Film film) throws ValidationException {
        film.setId(filmId++);
        filmHashMap.put(film.getId(), film);
        log.info("Конец обработки запроса по добавлению фильма {} и его id {}", film, film.getId());
        return film;
    }

    @Override
    public void deleteFilm(long id) {
        if (filmHashMap.containsKey(id)) {
            filmHashMap.remove(id);
        } else {
            System.out.println("Фильм с данным id не найден");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmHashMap.containsKey(film.getId())) {
            filmHashMap.put(film.getId(), film);
            return film;
        } else {
            System.out.println("Фильм не найден");
            return null;
        }
    }

    public List<Long> getFilmHashKey() {
        return new ArrayList<>(filmHashMap.keySet());
    }

    @Override
    public Film getFilm(long id) {

        return filmHashMap.get(id);
    }
}