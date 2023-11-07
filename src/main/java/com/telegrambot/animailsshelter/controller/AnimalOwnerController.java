package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.Animal;
import com.telegrambot.animailsshelter.model.AnimalOwner;
import com.telegrambot.animailsshelter.repository.AnimalOwnerRepository;
import com.telegrambot.animailsshelter.service.AddService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/animal-owner")
@RestController
public class AnimalOwnerController {
    private final AddService addService;
    private final AnimalOwnerRepository animalOwnerRepository;
    public AnimalOwnerController(AddService addService,
                                 AnimalOwnerRepository animalOwnerRepository) {
        this.addService = addService;
        this.animalOwnerRepository = animalOwnerRepository;
    }
    // добавление Владельца животного
    @PostMapping("/{id}/{name}/{phoneNumber}/{eMail}/{trialPeriod}")
    public void AnimalOwnerSave(@PathVariable long id,
                                @PathVariable boolean trialPeriod) {
        addService.animalOwnerSave(id, trialPeriod);
    }
    //поиск по Id Владельца животного
    @GetMapping("/{id}")
    public Optional<AnimalOwner> findById(@PathVariable Long id) {
        return animalOwnerRepository.findById(id);
    }
    //поиск всех Владельцев животных
    @GetMapping("/all")
    public List<AnimalOwner> findAll() {
        return animalOwnerRepository.findAll();
    }
    //редактирование по Id Владельца животного
    @PutMapping("/{id}")
    public AnimalOwner updateAnimal(long id,String name, String phoneNumber, String eMail, boolean trialPeriod) {
        return animalOwnerRepository.updateById(id, name, phoneNumber, eMail, trialPeriod);
    }
    //удаление Владельца животного
    @DeleteMapping("/{id}")
    public void deleteAnimal(@PathVariable Long id) {
        animalOwnerRepository.deleteById(id);
    }


}
