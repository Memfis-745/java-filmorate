package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final Validation validation;

    public List<Film> getAllFilms() {
        return filmRepository.findAllFilms();

    }

    public Film getFilm(long id) {
        return filmRepository.getFilm(id).orElseThrow(() -> new NotFoundException("Фильм с id - " + id + " не найден"));
    }

    public List<Film> getPopularFilms(Integer count) {

        if (count == null) {
            count = 10;
        }
        return filmRepository.getPopularFilms(count);
    }

    public Film createFilm(Film film) throws ValidationException {

        try {
            mpaRepository.isMpaExists(film.getMpa().getId());
        } catch (NotFoundException e) {
            throw new ConditionsNotMetException("Такого MPA не существует");
        }

        filmRepository.createFilm(film);

        if (film.getId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        try {
            genreRepository.saveGenre(film);
        } catch (NotFoundException e) {
            throw new ConditionsNotMetException("Такого жанра не существует");
        }
        log.info("Фильм {} добавлен в список", film);
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        if (filmRepository.isFilmExists(film.getId()) == null) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
        mpaRepository.isMpaExists(film.getMpa().getId());
        filmRepository.updateFilm(film);
        genreRepository.saveGenre(film);
        log.info("Фильм с id = {} обновлен", film.getId());
        log.info("Фильм с id = {} обновлен", film.getId());
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        filmRepository.isFilmExists(filmId);
        filmRepository.addLike(filmId, userId);
        log.info("Пользователь с id = {} поставил лайк фильму id = {}", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmRepository.isFilmExists(filmId);
        userRepository.isUserNotExists(userId);
        filmRepository.deleteLike(filmId, userId);
        log.info("Пользователь с id = {} удалил лайк фильму id = {}", userId, filmId);
    }
}

