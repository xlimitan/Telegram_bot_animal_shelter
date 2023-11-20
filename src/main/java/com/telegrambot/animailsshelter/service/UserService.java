package com.telegrambot.animailsshelter.service;

import com.telegrambot.animailsshelter.model.*;
import com.telegrambot.animailsshelter.repository.PetReportRepository;
import com.telegrambot.animailsshelter.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис для работы с пользователем
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PetReportRepository petReportRepository;

    public UserService(UserRepository userRepository, PetReportRepository petReportRepository) {
        this.userRepository = userRepository;
        this.petReportRepository = petReportRepository;
    }

    /**
     * Метод Сохраняет сущность User.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param chatId сохраняемая Id чата
     * @param firstName сохраняемая Имя
     * @param lastName  сохраняемая Фамилию
     * @param userName  сохраняемая Имя пользователя
     * @return возвращаемая сущность
     */

    public User saveBotUser(long chatId, String firstName, String lastName, String userName, String phoneNumber, String eMail, Long animal, LocalDate date, TrialPeriod period) {
        User user = new User(chatId, firstName, lastName, userName, phoneNumber,eMail,animal, date, period);
        return userRepository.save(user);
    }

    /**
     * Метод возвращает список пользователей
     * Используется метод репозитория
     * @return возвращает всех пользователей
    */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Метод возращает сущность User по ID
     * Используется метод репозитория {@link JpaRepository#findById(Object)}
     * @param chatId ID пользователя
     * @return возвращает сущность
     */
    public Optional<User> getUserById(Long chatId) {
        return userRepository.findById(chatId);
    }

    @Transactional
    public void savePhoneUser(long userId, String message){
        User user = userRepository.getReferenceById(userId);
        Pattern pattern = Pattern.compile("[0-9\\+]{12}+");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            user.setPhoneNumber(message);
            userRepository.save(user);
            SendMessage sendMessage = new SendMessage();
        }
    }
}
