package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    @Autowired
    final FilmStorage filmStorage;
    final UserService userService;
    final Validation validation;

    public FilmService(FilmStorage filmStorage, UserService userService, Validation validation) {

        this.filmStorage = filmStorage;
        this.userService = userService;
        this.validation = validation;
    }

    public List<Film> getAllFilms() {

        return filmStorage.showAllFilms();
    }

    public Film getFilm(long id) {
        validation.validId(id);
        validation.validFilm(id);
        return filmStorage.getFilm(id);
    }

    public List<Film> getPopularFilms(Integer count) {

        if (count == null) {
            count = 10;
        }

        List<Film> films = getAllFilms().stream().filter(e -> e.getLikes() != null).collect(Collectors.toList());
        films.sort((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()));

        List<Film> popularFilms = new ArrayList<>();
        if (films.size() > count) {
            for (int i = 0; i < count; i++) {
                popularFilms.add(films.get(i));
            }
        } else {
            for (int i = 0; i < films.size(); i++) {
                popularFilms.add(films.get(i));
            }
        }
        return popularFilms;
    }

    public Film createFilm(Film film) throws ValidationException {
        validation.validDate(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validation.validId(film.getId());
        validation.validFilm(film.getId());
        validation.validDate(film);
        return filmStorage.updateFilm(film);
    }

    public void addLike(Long filmId, Long userId) {
        try {
            validation.validId(userId);
            validation.validId(filmId);

            validation.validUser(userId);
            validation.validFilm(filmId);

            Film film = getFilm(filmId);
            User user = userService.getUser(userId);
            try {
                if (user.getFilmIdLiked() == null) {
                    log.info("Пользователь {} еще не оценил ни одного фильма", userId);
                    if (film.getLikes() == null) {
                        log.info("У фильма {} еще нет ни одного лайка", filmId);
                        log.info("Ставим пользователю {} лайк фильму {} и наоборот", userId, filmId);
                        Set<Long> like = new HashSet<>(Math.toIntExact(filmId));
                        user.setFilmIdLiked(like);
                        Set<Long> filmLike = new HashSet<>(Math.toIntExact(userId));
                        film.setLikes(filmLike);
                    } else {
                        log.info("Добавляем фильму {} лайк от пользователя {}", filmId, userId);
                        film.getLikes().add(userId);
                        log.info("Добавляем первый лайк пользователю {} к фильму {}", userId, filmId);
                        Set<Long> like = new HashSet<>(Math.toIntExact(filmId));
                        user.setFilmIdLiked(like);
                    }
                } else {
                    log.info("Пользователь {} уже оценил какие-то фильмы", userId);
                    if (film.getLikes() == null) {
                        Set<Long> filmLike = new HashSet<>(Math.toIntExact(userId));
                        log.info("У фильма {} не было ни одного лайка, ставим первый", filmId);
                        film.setLikes(filmLike);
                    }
                    log.info("Добавляем пользователю {} лайк фильма {} и наоборот", userId, filmId);
                    user.getFilmIdLiked().add(filmId);
                    film.getLikes().add(userId);
                }
            } catch (DuplicatedDataException e) {
                throw new DuplicatedDataException("Лайк пользователя к фильму уже поставлен");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный формат id и friendId");
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        try {
            validation.validId(userId);
            validation.validId(filmId);

            validation.validUser(userId);
            validation.validFilm(filmId);

            Film film = getFilm(filmId);
            User user = userService.getUser(userId);

            try {
                if (user.getFilmIdLiked() == null) {
                    log.info("Пользователь {} еще не cтавил лайки", userId);
                    if (film.getLikes() == null) {
                        log.info("У фильма {} еще нет ни одного лайка", filmId);
                    }
                } else {
                    log.info("Пользователь {} уже оценил какие-то фильмы", userId);
                    if (film.getLikes() == null) {
                        log.info("У фильма {} нет лайков", filmId);
                    }
                    log.info("Удаляем лайк у пользователя {} и фильма {}", userId, filmId);
                    user.getFilmIdLiked().add(filmId);
                    film.getLikes().add(userId);
                }
            } catch (DuplicatedDataException e) {
                throw new DuplicatedDataException("Лайк пользователя к фильму уже поставлен");
            }


        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный формат id и friendId");
        }
    }
}
