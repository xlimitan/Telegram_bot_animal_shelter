package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс UserRepository представляет репозиторий для управления информацией о пользователях бота.
 */
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByChatId(Long chatId);
}
