package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal,Long> {
}
