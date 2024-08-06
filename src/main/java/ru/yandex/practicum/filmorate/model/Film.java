package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(exclude = {"name", "description", "releaseDate", "duration"})
public class Film {
    private Long id = 0L;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

}
