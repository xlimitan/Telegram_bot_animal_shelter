package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.PetReport;
import com.telegrambot.animailsshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * Интерфейс UserRepository представляет репозиторий для управления информацией о пользователях бота.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByChatId(Long chatId);

        User getReferenceById(Long userId);
    @Query(value = "UPDATE public.users_data_table set date =:date WHERE chat_id=:chat_id", nativeQuery = true)
    void saveDateByChatId(Long chat_id, LocalDate date);
}
