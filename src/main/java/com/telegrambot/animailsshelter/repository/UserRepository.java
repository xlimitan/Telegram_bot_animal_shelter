package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


/**
 * Интерфейс UserRepository представляет репозиторий для управления информацией о пользователях бота.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByChatId(Long chatId);
}
