package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    List<Film> findAllFilms();

    Optional<Film> getFilm(Long id);

    void createFilm(Film film);

    void updateFilm(Film newFilm);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    List<Film> getPopularFilms(Integer count);

    Long isFilmExists(Long id);
}