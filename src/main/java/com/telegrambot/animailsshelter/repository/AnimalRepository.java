package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Интерфейс AnimalRepository представляет репозиторий для управления информацией о животном.
 */
public interface AnimalRepository extends JpaRepository<Animal,Long> {
    default Animal updateById(long animalId,String animalType, String name, int age, String breed) {
        return null;
    }
    void deleteById(Long animalId);
}
