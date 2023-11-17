package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.TrialPeriod;
import com.telegrambot.animailsshelter.model.User;
import com.telegrambot.animailsshelter.repository.UserRepository;
import com.telegrambot.animailsshelter.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
/**
 * Контроллер для работы с пользователем
 */
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }
    @GetMapping("/chatId/{chatId}")
    public ResponseEntity<User> findByChatId(@PathVariable @Positive Long chatId) {
        return ResponseEntity.of(userRepository.findByChatId(chatId));
    }
    @PostMapping
    public ResponseEntity<User> save(@RequestBody @Valid User user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{chatId}")
    public ResponseEntity<String> deleteById(@PathVariable @Positive Long chatId) {
        try {
            userRepository.deleteById(chatId);
            return ResponseEntity.ok().body("Запись удалена");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка удаления:" + e.getMessage());
        }
    }

    @GetMapping("/{chatId}/saveDate")
    public void saveDateByUserId(@RequestParam Long userId) {
        Optional<User> optionalUser = userRepository.findByChatId(userId);
        User user = optionalUser.get();
        user.setDate(LocalDate.now());
        userRepository.save(user);
    }

    @GetMapping("/{userId}/savePeriod")
    public void savePeriodByUserId(@RequestParam Long userId, TrialPeriod period) {
        Optional<User> optionalUser = userRepository.findByChatId(userId);
        User user = optionalUser.get();
        user.setPeriod(period);
        userRepository.save(user);
    }
}

