package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.Animal;
import com.telegrambot.animailsshelter.model.Shelter;
import com.telegrambot.animailsshelter.repository.ShelterRepository;
import com.telegrambot.animailsshelter.service.AddService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/shelter")
@RestController
public class ShelterController {
    private final AddService addService;
    private final ShelterRepository shelterRepository;

    public ShelterController(AddService addService,
                             ShelterRepository shelterRepository) {
        this.addService = addService;
        this.shelterRepository = shelterRepository;
    }
    // добавление приюта
    @PostMapping("/{id}/{shelterType}/{shelterName}/{address}/{information}")
    public void ShelterSave(@PathVariable long id,
                            @PathVariable String shelterType,
                            @PathVariable String shelterName,
                            @PathVariable String address,
                            @PathVariable String information) {
        addService.shelterSave(id,shelterType, shelterName, address, information);
    }
    //поиск всех приютов
    @GetMapping("/all")
    public List<Shelter> findAll() {
        return shelterRepository.findAll();
    }
    //поиск по Id приюта
    @GetMapping("/{id}")
    public Optional<Shelter> findById(long id) {
        return shelterRepository.findById(id);
    }
    //удаление приюта
    @DeleteMapping("/{id}")
    public void deleteById(long id) {
        shelterRepository.deleteById(id);
    }
}
