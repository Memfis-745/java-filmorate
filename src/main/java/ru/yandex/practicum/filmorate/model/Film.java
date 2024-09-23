package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.utils.NotBeforeDate;
import ru.yandex.practicum.filmorate.utils.WorkInterface;

import java.util.LinkedHashSet;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"name", "description", "releaseDate", "duration"})
public class Film {
    @NotNull(groups = WorkInterface.Update.class)
    private Long id = 0L;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotBlank(groups = WorkInterface.Create.class)
    @Size(max = 200, message = "Описание не может быть больше 200 знаков")
    private String description;
    @NotBeforeDate
    @NotNull(groups = WorkInterface.Create.class)
    private LocalDate releaseDate;
    @Positive
    @NotNull(groups = WorkInterface.Create.class)
    private int duration;
    @NotNull
    Mpa mpa;
    @Builder.Default
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();


}

