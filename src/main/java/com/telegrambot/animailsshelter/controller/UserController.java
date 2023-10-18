package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.User;
import com.telegrambot.animailsshelter.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Создание нового пользователя
    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // Получение информации о пользователе по ID
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long Id) {
        try {
            Optional<User> user = userRepository.findById(Id);
            if (user.isPresent()) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // "Получение списка всех посетителей"
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }
    //"Получение посетителя по Telegram chatId"
    @GetMapping("/chatId/{chatId}")
    public ResponseEntity<User> findByChatId(@PathVariable @Positive Long chatId) {
        return ResponseEntity.of(userRepository.findByChatId(chatId));
    }
    //"Обновление информации о посетителе по ID"
    @PutMapping("/{id}")
    public ResponseEntity<User> updateById(
            @PathVariable Long id,
            @RequestParam(name = "chatId", required = false) @Positive Long chatId,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName) {
        try {
            User User = userRepository.findById(id).orElseThrow();
            User.setChatId(chatId);
            User.setFirstName(firstName);
            User.setLastName(lastName);
            return ResponseEntity.ok(userRepository.save(User));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка обновления:" + e.getMessage());
        }
    }
    //"Удаление посетителя из приюта, соответствующей переданному в параметре ID"
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable @Positive Long id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.ok().body("Запись удалена");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка удаления:" + e.getMessage());
        }
    }
}
