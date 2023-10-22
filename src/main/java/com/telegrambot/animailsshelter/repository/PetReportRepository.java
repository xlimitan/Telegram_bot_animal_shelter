package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.PetReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetReportRepository extends JpaRepository<PetReport,Long> {
}
