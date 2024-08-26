package ru.yandex.practicum.filmorate.storage;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.*;

@Service
public interface FilmStorage {

    public Film createFilm(Film film) throws ValidationException;

    public void deleteFilm(long id);

    public Film updateFilm(Film film);

    public Film getFilm(long id);

    public List<Film> showAllFilms();

    public default HashMap<Long, Film> getFilmHashMap() {
        return null;
    }

    ;

    // Map<Object, Object> getFilmHashMap();
}
