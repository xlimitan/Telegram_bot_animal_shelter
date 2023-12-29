package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.PetReport;
import com.telegrambot.animailsshelter.model.PhotoReport;
import com.telegrambot.animailsshelter.repository.PetReportRepository;
import com.telegrambot.animailsshelter.repository.PhotoReportRepository;
import com.telegrambot.animailsshelter.service.AddService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequestMapping("/pet-report")
@RestController
public class PetReportController {
    private final AddService addService;
    private final PetReportRepository petReportRepository;
    private final PhotoReportRepository photoReportRepository;

    public PetReportController(AddService addService,
                               PetReportRepository petReportRepository,
                               PhotoReportRepository photoReportRepository) {
        this.addService = addService;
        this.petReportRepository = petReportRepository;
        this.photoReportRepository = photoReportRepository;
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

    //поиск по Id  фото отчёта
    @GetMapping("/photos/{id}")
    public PhotoReport findByIdPhotoReport(long chatId) {
        return photoReportRepository.findPhotoReportById(chatId);
    }
}
