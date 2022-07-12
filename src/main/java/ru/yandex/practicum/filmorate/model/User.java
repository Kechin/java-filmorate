package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
@Data
public class User {
//    целочисленный идентификатор — id;
//    электронная почта — email;
//    логин пользователя — login;
//    имя для отображения — name;
//    дата рождения — birthday.

    Integer id;
    @Email(message = "Неверный e-mail")
    String email;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{1,}$",message = "Недопустимый логин.")
    String login;
    String name;
    @Past (message = "Недопустимая дата.")
    LocalDate birthday;

    public User(Integer id, String email, String login, String name, LocalDate date) {
        this.id=id;
        this.email=email;
        this.login=login;
        this.name=name;
        this.birthday=date;
    }
}
