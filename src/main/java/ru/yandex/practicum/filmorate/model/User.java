package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = {"email", "login", "name", "birthday"})
public class User {
    private Long id = 0L;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

}
