package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Интерфейс VolunteerRepository представляет репозиторий для управления информацией о волонтёре.
 */
public interface VolunteerRepository extends JpaRepository<Volunteer,Long> {
}
