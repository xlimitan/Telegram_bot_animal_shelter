package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.PetReport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс PetReportRepository представляет репозиторий для управления информацией об отчётах животных.
 */
public interface PetReportRepository extends JpaRepository<PetReport,Long> {
}
