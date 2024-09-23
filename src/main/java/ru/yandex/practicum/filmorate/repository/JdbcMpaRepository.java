package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.MpaMapper;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final MpaMapper mapper;

    @Override
    public Collection<Mpa> findAll() {
        String sql = """
                SELECT *
                FROM MPA;
                """;
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<Mpa> getById(int id) {
        String sql = """
                SELECT *
                FROM MPA
                WHERE MPA_ID = :mpa_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource().addValue("mpa_id", id);
        return Optional.ofNullable(jdbc.queryForObject(sql, parameter, mapper));
    }

    @Override
    public void isMpaExists(int id) {
        try {
            log.info("Заходим в метод проверки существования MPA", id);
            getById(id).get().getName();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинг MPA с id = " + id + " не найден!");
        }
    }

}
