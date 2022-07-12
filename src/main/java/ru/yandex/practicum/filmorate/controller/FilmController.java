package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private final LocalDate MIN_DATE=LocalDate.of(1895,12,28);
    private int id;
    private Map<Integer,Film> films = new HashMap<>();
    @GetMapping("/films")
    public Collection<Film> findAll(){
        log.info("Получен запрос на получение списка фильмов");
        return films.values();
    }
    @PostMapping("/films")
    public Film create (@Valid @RequestBody Film newFilm, BindingResult bindingResult){
        if (bindingResult.hasErrors()||newFilm.getReleaseDate().isBefore(MIN_DATE)){
            throw new ValidationException
                    ("Ошибка при добавлении нового фильма: "+newFilm.getName()+bindingResult.getFieldError());
        }
        newFilm.setId(++id);
        films.put(newFilm.getId(), newFilm);
        log.info("Успешно добавлен фильм"+newFilm.getName());
        return newFilm;
    }
    @PutMapping("/films")
    public Film updateUser (@Valid @RequestBody Film film,BindingResult bindingResult){
        if (bindingResult.hasErrors()|| film.getReleaseDate().isBefore(MIN_DATE)||!films.containsKey(film.getId())){
            throw new ValidationException
                    ("Ошибка при обновлении карточки фильма: "+film.getName()+bindingResult.getFieldError());
        }
        films.put(film.getId(), film);
        log.info("Карточка фильма "+film.getName()+" успешно обновлена");
        return film;
    }
}
