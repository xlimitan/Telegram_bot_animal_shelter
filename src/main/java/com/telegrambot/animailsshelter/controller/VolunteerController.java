package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.Shelter;
import com.telegrambot.animailsshelter.model.Volunteer;
import com.telegrambot.animailsshelter.repository.VolunteerRepository;
import com.telegrambot.animailsshelter.service.AddService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/volunteer")
@RestController
public class VolunteerController {
    private final AddService addService;
    private final VolunteerRepository volunteerRepository;

    public VolunteerController(AddService addService,
                               VolunteerRepository volunteerRepository) {
        this.addService = addService;
        this.volunteerRepository = volunteerRepository;
    }
    // добавление волонтёра
    @PostMapping("/{id}/{name}/{phoneNumber}")
    public void VolunteerSave(@PathVariable long id ,
                              @PathVariable String name,
                              @PathVariable String phoneNumber) {
        addService.volunteerSave(id, name, phoneNumber);
    }
    //поиск всех волонтёров
    @GetMapping("/all")
    public List<Volunteer> findAll() {
        return volunteerRepository.findAll();
    }
    //поиск по Id волонтёра
    @GetMapping("/{id}")
    public Optional<Volunteer> findById(long id) {
        return volunteerRepository.findById(id);
    }
    //удаление волонтёра
    @DeleteMapping("/{id}")
    public void deleteById(long id) {
        volunteerRepository.deleteById(id);
    }
}
