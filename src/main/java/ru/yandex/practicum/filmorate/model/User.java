package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.utils.WorkInterface;

import java.time.LocalDate;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"email", "login", "name", "birthday"})
public class User {
    @NotNull(groups = WorkInterface.Update.class)
    private Long id = 0L;
    @Email
    @NotBlank(groups = WorkInterface.Create.class)
    private String email;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я._-]+$")
    private String login;
    private String name;
    @NotNull(groups = WorkInterface.Create.class)
    @PastOrPresent
    private LocalDate birthday;
    Set<Integer> friends = new HashSet<>();


}
