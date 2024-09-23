package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.repository.UserRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class Validation {


    private final UserRepository userRepository;


    public void validId(Long id) {
        if (id == null) {
            throw new ConditionsNotMetException("id не должен быть null");
        }
    }

    public void validUser(Long id) {
        if (userRepository.getUser(id).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    public void userName(User user) {
        log.trace("Проверка имени нового пользователя {}", user.getName());
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void validUserId(User user) {
        if (user.getId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        if (userRepository.getUser(user.getId()).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не создан");
        }
    }

}