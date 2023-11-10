package com.telegrambot.animailsshelter.service;

import com.telegrambot.animailsshelter.model.PhotoReport;
import com.telegrambot.animailsshelter.repository.PhotoReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PhotoReportService {
    private final PhotoReportRepository photoReportRepository;

    public PhotoReportService(PhotoReportRepository photoReportRepository) {
        this.photoReportRepository = photoReportRepository;
    }

    /**
     * добавление фото отчета в БД
     * @param photoReport фото отчет
     * @return фото отчет
     */
    public PhotoReport addPhotoReport(PhotoReport photoReport){
        photoReportRepository.save(photoReport);
        return photoReport;
    }

    /**
     * поиск фото в отчете по владельцу и дате добавления
     * @param userId владельца
     * @param date дата
     * @return фото отчет
     */
    public PhotoReport findPhotoReportByUserIdAndDate(Long userId, LocalDate date){
        return photoReportRepository.findPhotoReportByUserIdAndDate(userId, date);
    }

    /**
     * перезапись фото в БД
     * @param chatId пользователя
     * @param dir путь где хранится фото
     * @return фото отчет
     */
    public PhotoReport recordDirPhoto(long chatId, String dir){
        return photoReportRepository.recordDirPhoto(chatId, dir);
    }
}
