package com.telegrambot.animailsshelter.repository;

import com.telegrambot.animailsshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
