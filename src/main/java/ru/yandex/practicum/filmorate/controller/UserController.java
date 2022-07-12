package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class UserController {
    private Map<Integer,User> users = new HashMap<>();
    private int id =0;
    @GetMapping("/users")
    public Collection<User> findAll(){
        log.info("Получен запрос на получение списка пользователей");
        return users.values();
    }
    @PostMapping("/users")
    public User create (@Valid @RequestBody User newUser, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            throw new ValidationException("Ошибка при добавлении нового пользователя: "+newUser.getEmail()+bindingResult.getFieldError());
        }
        if (newUser.getName()==""){
           newUser.setName(newUser.getLogin());
       }
        newUser.setId(++id);
        users.put(id,newUser);
        log.info("Новый пользователь "+ newUser +" успешно добавлен");
        return newUser;
    }
    @PutMapping("/users")
    public User updateUser (@Valid @RequestBody User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()||!users.containsKey(user.getId())){
            throw new ValidationException("Ошибка при обновлении данных пользователя: "+user.getEmail());
        }

        users.put(user.getId(),user);
        log.info("Данные пользователя "+ user.getEmail() +" успешно обновлены");
        return  user;
    }
}
