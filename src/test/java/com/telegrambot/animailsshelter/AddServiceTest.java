package com.telegrambot.animailsshelter;

import com.telegrambot.animailsshelter.model.Animal;
import com.telegrambot.animailsshelter.model.Shelter;
import com.telegrambot.animailsshelter.model.Volunteer;
import com.telegrambot.animailsshelter.repository.AnimalRepository;
import com.telegrambot.animailsshelter.repository.PetReportRepository;
import com.telegrambot.animailsshelter.repository.ShelterRepository;
import com.telegrambot.animailsshelter.repository.VolunteerRepository;
import com.telegrambot.animailsshelter.service.AddService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = AddServiceTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddServiceTest {


    @MockBean
    private AnimalRepository animalRepository;

    @MockBean
    private PetReportRepository petReportRepository;

    @MockBean
    private ShelterRepository shelterRepository;

    @MockBean
    private VolunteerRepository volunteerRepository;

    private AddService addService;


    @Test
    public void testAnimalSave() {
        Animal animal = new Animal(1L, "кот", "Мурзик", 2, "шотландская вислоухая");
        Mockito.when(animalRepository.save(animal)).thenReturn(animal);

        Animal savedAnimal = addService.animalSave(1L, "кот", "Мурзик", 2, "шотландская вислоухая");

        assertEquals(animal, savedAnimal);
    }



    @Test
    public void testShelterSave() {
        Shelter shelter = new Shelter(1L, "для кошек", "Кошачий рай", "123 Main St", "Прекрасный приют для кошек");
        Mockito.when(shelterRepository.save(shelter)).thenReturn(shelter);

        Shelter savedShelter = addService.shelterSave(1L, "для кошек", "Кошачий рай", "123 Main St", "Прекрасный приют для кошек");

        assertEquals(shelter, savedShelter);
    }

    @Test
    public void testVolunteerSave() {
        Volunteer volunteer = new Volunteer(1L, "Вася", "9876543210");
        Mockito.when(volunteerRepository.save(volunteer)).thenReturn(volunteer);

        Volunteer savedVolunteer = addService.volunteerSave(1L, "Вася", "9876543210");

        assertEquals(volunteer, savedVolunteer);
    }
}
