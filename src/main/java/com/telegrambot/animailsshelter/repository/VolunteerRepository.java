package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer,Long> {
}
