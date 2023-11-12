package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.PhotoReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PhotoReportRepository extends JpaRepository<PhotoReport,Long> {
    PhotoReport findPhotoReportByUserIdAndDate(Long chatId, LocalDate date);
    PhotoReport findPhotoReportById(Long chatId);
   /* List<PhotoReport> findAllPhotoReportByUserId(Long chatId);*/
    @Modifying
    @Query(value = "UPDATE public.photo_report set path =:dir WHERE user_id =:id ", nativeQuery = true)
    void recordDirPhoto(long id, String dir);
}
