package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;
    private final Validation validation;

    public Collection<Mpa> findAll() {
        log.info("Получение списка рейтингов");
        return mpaRepository.findAll();
    }

    public Mpa getById(int id) {
        log.info("Получение рейтинга с id = {}", id);
        mpaRepository.isMpaExists(id);
        return mpaRepository.getById(id).orElseThrow(() -> new NotFoundException(
                "MPA c ID - " + id + " не найден"));
    }
}