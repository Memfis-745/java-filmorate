package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

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
    @NotNull
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotNull
    @Size(max = 200, message = "Описание не может быть больше 200 знаков")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private int duration;

}
