package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Интерфейс ShelterRepository представляет репозиторий для управления информацией о приюте.
 */
public interface ShelterRepository extends JpaRepository<Shelter,Long> {
}
