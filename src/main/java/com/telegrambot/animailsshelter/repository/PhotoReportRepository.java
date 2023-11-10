package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.PhotoReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface PhotoReportRepository extends JpaRepository<PhotoReport,Long> {
    PhotoReport findPhotoReportByUserIdAndDate(Long chatId, LocalDate date);

    @Query(value = "UPDATE public.photo_report set path =:dir WHERE user_id =:id ", nativeQuery = true)
    PhotoReport recordDirPhoto(long id, String dir);
}
