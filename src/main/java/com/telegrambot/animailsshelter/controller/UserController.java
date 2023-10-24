package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.User;
import com.telegrambot.animailsshelter.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
/**
 * Контроллер для работы с пользователем
 */
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    // добавление пользователя
    @PostMapping("/")
    public User createUser(long id,long chatId,String firstName,String lastName,String userName) {
        return userService.saveBotUser(id, chatId,firstName, lastName, userName);
    }
    //поиск всех пользователей
   @GetMapping("/")
      public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    //поиск по Id пользователя
    @GetMapping("/{id}")
    public Optional<User> getUserById(Long id) {
        return userService.getUserById(id);
    }
    //редактирование пользователя
    @PutMapping("/{id}")
    public Integer updateUser(long id, long chatId, String firstName, String lastName, String userName) {
        return userService.updateUser(id, chatId,firstName, lastName, userName);
    }
    //удаление пользователя
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

