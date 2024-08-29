package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class Validation {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public void validId(Long id) {
        if (id == null) {
            throw new ConditionsNotMetException("id не должен быть null");
        }
    }

    public void validUser(Long id) {
        if (!userStorage.getUserHashKey().contains(id)) {
            throw new NotFoundException("Юзер с id = " + id + " не найден");
        }
    }

    public void validUserFriends(Long id) {
        User user = userStorage.getUser(id);
        if (user.getFriends() == null) {
            throw new NotFoundException("У пользователя = " + user + " нет друзей");
        }
    }

    public void userName(User user) {
        log.trace("Проверка имени нового пользователя {}", user.getName());
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void validFilm(Long id) {
        if (!filmStorage.getFilmHashKey().contains(id)) {
            throw new NotFoundException("Юзер с id = " + id + " не найден");
        }
    }

    public void validDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ConditionsNotMetException("Дата не может быть раньше 28.12.1895");
        }
    }

}