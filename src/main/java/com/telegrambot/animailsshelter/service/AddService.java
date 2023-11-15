package com.telegrambot.animailsshelter.service;

import com.telegrambot.animailsshelter.model.*;
import com.telegrambot.animailsshelter.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Класс AddService - это класс для добавления в базу данных
 * Он предоставляет интерфейс добавлеения таких сущностей как Animal, AnimalOwner, PetReport, Shelter, Volunteer в базу данных.
 */
@Service
public class AddService {
    private final AnimalOwnerRepository animalOwnerRepository;
    private final AnimalRepository animalRepository;
    private final PetReportRepository petReportRepository;
    private final ShelterRepository shelterRepository;
    private final VolunteerRepository volunteerRepository;
    private final UserRepository userRepository;

    public AddService(AnimalOwnerRepository animalOwnerRepository,
                      AnimalRepository animalRepository,
                      PetReportRepository petReportRepository,
                      ShelterRepository shelterRepository,
                      VolunteerRepository volunteerRepository, UserRepository userRepository) {
        this.animalOwnerRepository = animalOwnerRepository;
        this.animalRepository = animalRepository;
        this.petReportRepository = petReportRepository;
        this.shelterRepository = shelterRepository;
        this.volunteerRepository = volunteerRepository;
        this.userRepository = userRepository;
    }
/**
 * Метод сохраняет животных в БД
 * @param animalType тип животного кот или собака
 * @param name имя животного
 * @param age возраст животного
 * @param breed порода
 */
    public Animal animalSave( long id,String animalType, String name, int age, String breed) {
        Animal animal = new Animal(id,animalType, name, age, breed);
        return animalRepository.save(animal);
    }
/**
* Метод сохраняет владельца животного кота или собаку
* @param name имя
* @param phoneNumber номер телефона владельца
* @param eMail электронная почта
* @param trialPeriod испытательный срок
*/
    public AnimalOwner animalOwnerSave(long id, boolean trialPeriod) {
        AnimalOwner animalOwner = new AnimalOwner(id,trialPeriod);
        return animalOwnerRepository.save(animalOwner);
    }
/**
* Метод сохраняет отчёт о домашнем животном
* @param user диета
* @param report состояние животного
*/
    public PetReport petReportSave(User user, String report) {
        LocalDate localDate = LocalDate.now();
        PetReport petReport = new PetReport(user, report, localDate);
        return petReportRepository.save(petReport);
    }
/**
* Метод сохраняет данные о приюте
* @param shelterType  тип приюта для кошек или собак
* @param shelterName  названия приюта
* @param address  адрес приюта
*/
    public Shelter shelterSave(long id,String shelterType, String shelterName, String address, String information) {
        Shelter shelter = new Shelter(id,shelterType, shelterName, address, information);
        return shelterRepository.save(shelter);
    }
/**
* Метод сохраняет волонтёра
* @param name имя
* @param phoneNumber номер телефона волонтёра
*/
    public Volunteer volunteerSave(long id,String name, String phoneNumber) {
        Volunteer volunteer = new Volunteer(id,name, phoneNumber);
        return volunteerRepository.save(volunteer);
    }
}
