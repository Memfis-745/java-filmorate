package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.repository.mapper.FilmsMapper;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final FilmMapper filmExtractor;
    private final FilmsMapper filmsExtractor;

    @Override
    public List<Film> findAllFilms() {
        String sql = """
                SELECT *
                FROM FILMS AS f
                LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID
                LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID;
                """;

        return jdbc.query(sql, filmsExtractor);
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        String sql = """
                SELECT *
                FROM FILMS AS f
                LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID
                LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                WHERE f.FILM_ID = :film_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource("film_id", id);
        return Optional.ofNullable(jdbc.query(sql, parameter, filmExtractor));
    }

    @Override
    public void createFilm(Film film) {
        log.info("Заходим в метод createFilm в jdbc с фильмом: {}", film);

        String sql = """
                INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE, DURATION, MPA_ID)
                VALUES (:name, :description, :release, :duration, :mpa_id);
                """;

        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("Пытаемся добавить его в таблицу: ");

        jdbc.update(sql, parameter, keyHolder);
        log.info("Добавили. Получаем id: {}", film);
        Long id = keyHolder.getKeyAs(Long.class);
        film.setId(id);
        log.info("Смотрим получилось или нет: {}", film);
    }

    @Override
    public void updateFilm(Film film) {
        String sql = """
                UPDATE FILMS
                SET NAME = :name,
                DESCRIPTION = :description,
                RELEASE = :release,
                DURATION = :duration,
                MPA_ID = :mpa_id
                WHERE FILM_ID = :film_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId())
                .addValue("film_id", film.getId());
        jdbc.update(sql, parameter);
    }

    @Override
    public void addLike(Long id, Long userId) {
        String sql = """
                INSERT INTO FILMUSERLIKES (FILM_ID, USER_ID)
                VALUES (:film_id, :user_id)
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("film_id", id)
                .addValue("user_id", userId);
        jdbc.update(sql, parameter);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        String sql = """
                DELETE FROM FilmUserLikes
                WHERE FILM_ID = :film_id
                      AND USER_ID = :user_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("film_id", id)
                .addValue("user_id", userId);
        jdbc.update(sql, parameter);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sql = """
                SELECT COUNT(l.user_id) AS sum_likes,
                        f.FILM_ID,
                        f.NAME,
                        f.DESCRIPTION,
                        f.RELEASE,
                        f.DURATION,
                        f.MPA_ID,
                        m.MPA_NAME,
                        fg.GENRE_ID,
                        g.GENRE_NAME
                FROM FILMS AS f
                        LEFT JOIN FILMUSERLIKES AS l ON f.film_id=l.film_id
                        LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID
                        LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                        LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                GROUP BY f.NAME, f.FILM_ID, f.DESCRIPTION, f.RELEASE,
                                            f.DURATION, f.MPA_ID, m.MPA_NAME, fg.GENRE_ID, g.GENRE_NAME
                ORDER BY COUNT(l.user_id) DESC
                LIMIT :count;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource("count", count);

        return jdbc.query(sql, parameter, filmsExtractor);
    }

    @Override
    public Long isFilmExists(Long id) {
        if (getFilm(id).isEmpty()) {
            return null;
        } else return id;
    }
}
