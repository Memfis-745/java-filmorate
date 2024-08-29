package ru.yandex.practicum.filmorate.storage;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.*;

@Service
public interface FilmStorage {

    Film createFilm(Film film) throws ValidationException;

    void deleteFilm(long id);

    Film updateFilm(Film film);

    Film getFilm(long id);

    List<Film> showAllFilms();

    List<Long> getFilmHashKey();

}
