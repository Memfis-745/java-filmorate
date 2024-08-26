package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"name", "description", "releaseDate", "duration"})
public class Film {
    private Long id = 0L;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotNull
    @Size(max = 200, message = "Описание не может быть больше 200 знаков")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private int duration;
    Set<Long> likes;

}
