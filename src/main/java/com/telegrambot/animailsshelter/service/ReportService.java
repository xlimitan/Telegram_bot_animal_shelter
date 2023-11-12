package com.telegrambot.animailsshelter.service;

import ch.qos.logback.classic.Logger;
import com.telegrambot.animailsshelter.config.BotConfig;
import com.telegrambot.animailsshelter.model.PetReport;
import com.telegrambot.animailsshelter.model.Photo;
import com.telegrambot.animailsshelter.model.User;
import com.telegrambot.animailsshelter.repository.PetReportRepository;
import com.telegrambot.animailsshelter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ReportService{
    private Logger log;

    private final BotConfig config;
    @Autowired
    private UserRepository userRepository;
    private final Map<Long, String> userShelterChoiceMap;
    private final UserService userService;
    private final PetReportRepository petReportRepository;

    public ReportService(BotConfig config, UserService userService, PetReportRepository petReportRepository) {
        this.config = config;
        this.userService = userService;
        this.petReportRepository = petReportRepository;
        this.userShelterChoiceMap = new HashMap<>();
    }
/*
    private void sendText(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
    }

    private boolean isValidAdoptionReport(String reportText) {
        // Проверка на правильность и полноценность отчета
        return true;
    }

    private void saveAdoptionReport(Long animalId, String reportText, List<PhotoSize> photos) {
    }

    @Scheduled(fixedRate = 3600000)
    public void processAdoptionReports() {
        // Перебрать все записи в таблице отчетов
        // Если столбец с булевыми значениями равен null перейти к следующей записи
        // Если столбец равен true отправить уведомление об успешном отчете
        // Если столбец равен false отправить уведомление о том, что отчет не принят
    }

    public void saveTextReport(long id, String text, Photo  photo){
        PetReport petReport = petReportRepository.findByUser_ChatIdAndDate(id, LocalDateTime.now());
        petReportRepository.saveText(petReport.getId(), text);
    }*/

    @Scheduled(cron = "* * 21 * * *")
    public void checkingReports() {

    }
}

