package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
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
    Set<Long> friends;
    Set<Long> filmIdLiked;

}
