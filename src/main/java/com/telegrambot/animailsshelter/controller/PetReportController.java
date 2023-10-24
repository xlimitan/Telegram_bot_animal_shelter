package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.PetReport;
import com.telegrambot.animailsshelter.model.Volunteer;
import com.telegrambot.animailsshelter.repository.PetReportRepository;
import com.telegrambot.animailsshelter.service.AddService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/pet-report")
@RestController
public class PetReportController {
    private final AddService addService;
    private final PetReportRepository petReportRepository;
    public PetReportController(AddService addService,
                               PetReportRepository petReportRepository) {
        this.addService = addService;
        this.petReportRepository = petReportRepository;
    }
    // добавление отчёта о животном
    @PostMapping("/pet-report/{diet}/{feelings}/{check}")
    public void PetReportSave(@PathVariable long id,
                              @PathVariable String diet,
                              @PathVariable String feelings,
                              @PathVariable boolean check) {
        addService.petReportSave(id,diet, feelings, check);
    }
    //поиск всех отчётов о животных
    @GetMapping("/all")
    public List<PetReport> findAll() {
        return petReportRepository.findAll();
    }
    //поиск по Id  отчёта о животном
    @GetMapping("/{id}")
    public Optional<PetReport> findById(long id) {
        return petReportRepository.findById(id);
    }
    //удаление  отчёта о животном
    @DeleteMapping("/{id}")
    public void deleteById(long id) {
        petReportRepository.deleteById(id);
    }
}
