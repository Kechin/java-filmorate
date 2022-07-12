package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Data
public class Film {
//    целочисленный идентификатор — id;
//    название — name;
//    описание — description;
//    дата релиза — releaseDate;
//    продолжительность фильма — duration.
    Integer id;
    @NotBlank(message = "Пустое название фильма.")
    String name;
    @Size(max=200, message = "Превышена допустимая длина описания.")
    String description;

    LocalDate releaseDate;

    @Min(value = 0,message = "Недопустимая продолжительность фильма.")
    Integer duration;

    public Film(int id, String name, String description, LocalDate date, Integer duration) {
        this.id=id;
        this.name=name;
        this.description=description;
        this.releaseDate=date;
        this.duration=duration;
    }
}
