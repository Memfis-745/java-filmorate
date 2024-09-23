package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Collection<Genre> findAll() {
        log.info("Получение списка жанров");
        return genreRepository.findAll();
    }

    public Genre getById(int id) {
        log.info("Получение жанра с id = {}", id);
        genreRepository.isGenreNotExist(id);
        return genreRepository.getById(id).orElseThrow(() -> new NotFoundException("Жанр c ID - " + id + " не найден"));
    }
}