package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.PetReport;
import com.telegrambot.animailsshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Интерфейс PetReportRepository представляет репозиторий для управления информацией об отчётах животных.
 */
public interface PetReportRepository extends JpaRepository<PetReport,Long> {
    PetReport findByUser_ChatIdAndDate(Long chatId, LocalDate date);

    PetReport findReportByUser_ChatIdAndDate(Long chatId, LocalDateTime date);
    @Query(value = "UPDATE public.pet_report set report =:report WHERE id =:id ", nativeQuery = true)
    void saveText(long id, String report);
}
