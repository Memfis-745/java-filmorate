package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = {"email", "login", "name", "birthday"})
public class User {
    private Long id = 0L;
    @Email
    @NotEmpty
    private String email;
    @NotBlank
    //  @Pattern(regexp = "^[a-zA-Z0-9]", message = "Логин не должен содержать пробелов")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

}
