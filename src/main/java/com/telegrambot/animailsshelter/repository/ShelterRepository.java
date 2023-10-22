package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter,Long> {
}
