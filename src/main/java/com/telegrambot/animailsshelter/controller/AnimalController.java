package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.Animal;
import com.telegrambot.animailsshelter.repository.AnimalRepository;
import com.telegrambot.animailsshelter.service.AddService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/animal")
@RestController
public class AnimalController {
    private final AddService addService;
    private final AnimalRepository animalRepository;

    public AnimalController(AddService addService, AnimalRepository animalRepository) {
        this.addService = addService;
        this.animalRepository = animalRepository;
    }
    // добавление животного
    @PostMapping("/{id}/{animalType}/{name}/{age}/{breed}")
    public void AnimalSave(@PathVariable  long id ,
                           @PathVariable String animalType,
                           @PathVariable String name,
                           @PathVariable int age,
                           @PathVariable String breed) {
        addService.animalSave(id,animalType, name, age, breed);
    }
    //поиск по Id животного
    @GetMapping("/{id}")
    public Optional<Animal> findById(@PathVariable Long id) {
        return animalRepository.findById(id);
    }
    //поиск всех животных
    @GetMapping("/all")
    public List<Animal> findAll() {
        return animalRepository.findAll();
    }
    //редактирование по Id животного
    @PutMapping("/{id}")
    public Animal updateAnimal(long id, String animalType, String name, int age, String breed) {
        return animalRepository.updateById(id, animalType, name, age, breed);
    }
    //удаление животного
    @DeleteMapping("/{id}")
    public void deleteAnimal(@PathVariable Long id) {
        animalRepository.deleteById(id);
    }
}
