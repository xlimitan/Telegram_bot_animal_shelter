package com.telegrambot.animailsshelter.service;

import ch.qos.logback.classic.Logger;
import com.telegrambot.animailsshelter.config.BotConfig;
import com.telegrambot.animailsshelter.model.User;
import com.telegrambot.animailsshelter.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.util.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.telegrambot.animailsshelter.config.Information.HELLO;


/**
 * Класс TelegramBot - это основной класс Telegram-бота, который обрабатывает входящие обновления и сообщения.
 * Он предоставляет интерфейс для взаимодействия с пользователем и управления базой данных.
 */
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private Logger log;

    private final BotConfig config;
@Autowired
    private UserRepository userRepository;
    private final Map<Long, String> userShelterChoiceMap;

    public TelegramBot(BotConfig config) {
        this.config = config;
        this.userShelterChoiceMap = new HashMap<>();
    }

    /**
     * Обработчик входящих обновлений и сообщений от пользователей. Вызывается при получении нового обновления.
     *
     * @param update Объект, содержащий информацию об обновлении.
     */
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            long chatId = callbackQuery.getMessage().getChatId();

            if (userShelterChoiceMap.containsKey(chatId)) {
                if (callbackData.equals("backToShelterSelection")) {
                    userShelterChoiceMap.remove(chatId);
                    sendWelcomeMessage(chatId);
                } else {
                    sendErrorMessage(chatId);
                }
            } else if (callbackData.equals("catShelter") || callbackData.equals("dogShelter")) {
                userShelterChoiceMap.put(chatId, callbackData);
                sendMenuOptions(chatId);
            } else if (callbackData.equals("info")) {
                sendShelterInfo(chatId, userShelterChoiceMap.get(chatId));
            } else if (callbackData.equals("adopt")) {
                sendHowToAdoptInfo(chatId, userShelterChoiceMap.get(chatId));
            } else if (callbackData.equals("report")) {
                sendReportInstructions(chatId, userShelterChoiceMap.get(chatId));
            } else if (callbackData.equals("volunteer")) {
                callVolunteer(chatId);
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            if (messageText.equals("/start")) {
                sendWelcomeMessage(chatId);
            }
        }
    }


    /**
     * Отправляет сообщение об ошибке пользователю.
     *
     * @param chatId Идентификатор чата, куда отправить сообщение.
     */
    private void sendErrorMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Вы уже выбрали приют. Чтобы выбрать другой приют, вернитесь к предыдущему шагу выбора приюта.");

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Вернуться к выбору приюта");
        backButton.setCallbackData("backToShelterSelection");
        row1.add(backButton);

        keyboard.add(row1);
        markupKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(markupKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }



    /**
     * Отправляет приветственное сообщение пользователю при старте бота.
     *
     * @param chatId Идентификатор чата, куда отправить сообщение.
     */
    private void sendWelcomeMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        if (userRepository.findById(chatId).isEmpty()) {
            message.setText("Привет! Я бот, созданный для помощи с приютами для животных.\n\n" +
                    "Выберите приют для животных:\n");
        } else {
            message.setText("Выберите приют для животных:\n");
        }

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton infoButton = new InlineKeyboardButton();
        infoButton.setText("Приют для кошек");
        infoButton.setCallbackData("catShelter");
        row1.add(infoButton);

        InlineKeyboardButton adoptButton = new InlineKeyboardButton();
        adoptButton.setText("Приют для собак");
        adoptButton.setCallbackData("dogShelter");
        row1.add(adoptButton);

        keyboard.add(row1);
        markupKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(markupKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
//            log.error("Error occurred: " + e.getMessage());
        }
    }


    /**
     * Отправляет сообщение с меню опций для пользователя, позволяя выбрать действия.
     *
     * @param chatId Идентификатор чата, куда отправить сообщение.
     */
    private void sendMenuOptions(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите действие:");

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton infoButton = new InlineKeyboardButton();
        infoButton.setText("Узнать информацию о приюте");
        infoButton.setCallbackData("info");
        row1.add(infoButton);

        InlineKeyboardButton adoptButton = new InlineKeyboardButton();
        adoptButton.setText("Как взять животное из приюта");
        adoptButton.setCallbackData("adopt");
        row1.add(adoptButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton reportButton = new InlineKeyboardButton();
        reportButton.setText("Прислать отчет о питомце");
        reportButton.setCallbackData("report");
        row2.add(reportButton);

        InlineKeyboardButton volunteerButton = new InlineKeyboardButton();
        volunteerButton.setText("Позвать волонтера");
        volunteerButton.setCallbackData("volunteer");
        row2.add(volunteerButton);

        keyboard.add(row1);
        keyboard.add(row2);
        markupKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(markupKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
//            log.error("Error occurred: " + e.getMessage());
        }
    }


    /**
     * Регистрирует пользователя в системе и сохраняет информацию о нем в базе данных.
     *
     * @param message Объект сообщения, содержащего информацию о пользователе, который регистрируется.
     */
    private void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()){
            Long chatId = message.getChatId();
            Chat chat = message.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new TimeStamp());

            userRepository.save(user);
//            log.info("User saved: " + user);
        }
    }



    private void handleUserChoice(long chatId, String messageText, String shelterChoice) {
        // Обработку выбора пользователя в зависимости от приюта
        // Логика для каждого варианта
    }

    private void sendShelterInfo(long chatId, String shelterChoice) {
        // Логика для отправки информации о приюте
    }

    private void sendHowToAdoptInfo(long chatId, String shelterChoice) {
        // Логика для отправки информации о том, как взять животное из приюта
    }

    private void sendReportInstructions(long chatId, String shelterChoice) {
        // Логика для отправки инструкций по отправке отчета
    }

    private void callVolunteer(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Неизвестная команда. Вызываю волонтера для помощи.");
        try {
            execute(message);
        } catch (TelegramApiException e) {
//            log.error("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken(){
        return config.getToken();
    }
}

