package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.AnimalOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalOwnerRepository extends JpaRepository<AnimalOwner, Long> {
}
