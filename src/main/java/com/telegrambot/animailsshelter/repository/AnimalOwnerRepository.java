package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.AnimalOwner;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс AnimalOwnerRepository представляет репозиторий для управления информацией о владельце животного.
 */
public interface AnimalOwnerRepository extends JpaRepository<AnimalOwner, Long> {
    default AnimalOwner updateById(long id,String name, String phoneNumber, String eMail, boolean trialPeriod) {
        return null;
    }
}
