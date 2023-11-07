package com.telegrambot.animailsshelter.service;

import com.telegrambot.animailsshelter.model.User;
import com.telegrambot.animailsshelter.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Метод Сохраняет сущность User.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     *
     * @param id сохраняемая ID
     * @param chatId сохраняемая Id чата
     * @param firstName сохраняемая Имя
     * @param lastName сохраняемая Фамилию
     * @param userName сохраняемая Имя пользователя
     * @return возвращаемая сущность
     */

    public User saveBotUser(long chatId, String firstName, String lastName, String userName, String phoneNumber,String eMail) {
        User user = new User(chatId, firstName, lastName, userName, phoneNumber,eMail);
        return userRepository.save(user);
    }
    /**
     * Метод возращает список пользователей
     * Используется метод репозитория
     * @return возращает всех пользователей
    */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    /**
     * Метод возращает сущность User по ID
     * Используется метод репозитория {@link JpaRepository#findById(Object)}
     * @param id ID пользователя
     * @return возвращает сущность
     */
    public Optional<User> getUserById(Long chatId) {
        return userRepository.findById(chatId);
    }
    /**
     * Обновляет сущность по передаваемым параметрам.
     * Используется метод репозитория
     *
     * @param id  ID пользователя
     * @param chatId ID пользователя из чата
     * @param firstName Имя
     * @param lastName Фамилия
     * @param userName Имя пользователя
     * @return число ({@code 1} - сущность обновлена, {@code 0} - сущность не обновлена)
     */
  /*  public Integer updateUser(long id, long chatId, String firstName, String lastName, String userName) {
        Optional<User> user = getUserById(id);
        if (user.isEmpty()
                || (firstName == null || firstName.isBlank())
                || (lastName == null || lastName.isBlank())
                || (userName == null || userName.isBlank())) {
            return 0;
        } else {
            userRepository.updateById(id, chatId, firstName, lastName, userName);
            return 1;
        }
    }*/
    /**
     * Удаляет User по ID.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     *
     * @param id ID пользователя
     * @return {@code true} - сущность сохранена, {@code false} - сущность не сохранена
     */
    public Boolean deleteUser(Long chatId) {
        Optional<User> findUserById = getUserById(chatId);
        if (findUserById.isEmpty()) {
            return false;
        }
        userRepository.deleteById(chatId);
        return true;
    }

    public void savePhoneUser(long chatId, String message){
        User user = userRepository.findByChatId(chatId).orElseThrow();
        Pattern pattern = Pattern.compile("[0-9\\+]{12}+");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            user.setPhoneNumber(message);
            userRepository.save(user);
            SendMessage sendMessage = new SendMessage();
        }
    }
}
