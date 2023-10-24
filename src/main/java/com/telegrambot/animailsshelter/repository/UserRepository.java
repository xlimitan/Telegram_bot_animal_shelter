package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;


/**
 * Интерфейс UserRepository представляет репозиторий для управления информацией о пользователях бота.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByChatId(Long chatId);

    default User updateById(long id, long chatId, String firstName, String lastName, String userName) {
        return null;
    }
}