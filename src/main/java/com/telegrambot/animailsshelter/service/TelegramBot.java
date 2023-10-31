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

import static com.telegrambot.animailsshelter.config.CommandType.*;
import static com.telegrambot.animailsshelter.config.Information.*;


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
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasCallbackQuery()) {
//            CallbackQuery callbackQuery = update.getCallbackQuery();
//            String callbackData = callbackQuery.getData();
//            long chatId = callbackQuery.getMessage().getChatId();
//
//            if (userShelterChoiceMap.containsKey(chatId)) {
//                if (callbackData.equals("info")) {
//                    sendShelterInfo(chatId, userShelterChoiceMap.get(chatId));
//                } else if (callbackData.equals("adopt")) {
//                    sendHowToAdoptInfoForShelter(chatId, userShelterChoiceMap.get(chatId));
//                } else if (callbackData.equals("report")) {
//                    sendReportInstructions(chatId, userShelterChoiceMap.get(chatId));
//                } else if (callbackData.equals("volunteer")) {
//                    callVolunteer(chatId);
//                }
//            } else {
//                if (callbackData.equals("backToShelterSelection")) {
//                    userShelterChoiceMap.remove(chatId);
//                    sendWelcomeMessage(chatId);
//                } else if (callbackData.equals("catShelter") || callbackData.equals("dogShelter")) {
//                    userShelterChoiceMap.put(chatId, callbackData);
//                    sendMenuOptions(chatId);
//                } else {
//                    sendErrorMessage(chatId);
//                }
//            }
//        } else if (update.hasMessage() && update.getMessage().hasText()) {
//            long chatId = update.getMessage().getChatId();
//            String messageText = update.getMessage().getText();
//
//            if (messageText.equals("/start")) {
//                sendWelcomeMessage(chatId);
//                registerUser(update.getMessage());
//            }
//        }
//    }

    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        if (userShelterChoiceMap.containsKey(chatId)) {
            switch (callbackData) {
                case "info" -> sendText(chatId, INFO_SHELTER);
                case "adopt" -> sendHowToAdoptInfoForShelter(chatId, userShelterChoiceMap.get(chatId));
                case "becomeVolunteer" -> sendBecomeVolunteerInstructions(chatId);
                case "report" -> sendReportInstructions(chatId);
                case "volunteer" -> callVolunteer(chatId);
                case "aboutUs" -> aboutUs(chatId);
                case "address" -> sendText(chatId, ADDRESS_SHELTER);
                case "directions" -> sendText(chatId, DRIVING_DIRECTIONS);
                case "schedule" -> sendText(chatId, WORK_SCHEDULE);
                case "contact" -> sendText(chatId, SECURITY_DATA);
                case "safety" -> sendText(chatId, SAFETY_TECHNICAL);
                case "feedback" -> sendText(chatId, LEAVE_CONTACT_DETAILS);
                case "rule" -> sendText(chatId, RULES_OF_MEETING_A_PET);
                case "documents" -> sendText(chatId, REQUIRED_DOCUMENTS);
                case "transport" -> sendText(chatId, RECOMMENDATIONS_TRANSPORTATION);
                default -> sendErrorMessage(chatId);
            }
        } else {
            switch (callbackData) {
                case "backToShelterSelection" -> {
                    userShelterChoiceMap.remove(chatId);
                    sendWelcomeMessage(chatId);
                }
                case "catShelter", "dogShelter" -> {
                    userShelterChoiceMap.put(chatId, callbackData);
                    sendMenuOptions(chatId);
                }
                default -> sendErrorMessage(chatId);
            }
        }
    }

    private void handleMessage(Message message) {
        long chatId = message.getChatId();
        String messageText = message.getText();

        if (messageText.equals("/start")) {
            sendWelcomeMessage(chatId);
            registerUser(message);
        }
    }

    private void sendText(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //
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

        if (userRepository.findByChatId(chatId).isEmpty()) {
            message.setText(HELLO);
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

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton becomeVolunteerButton = new InlineKeyboardButton();
        becomeVolunteerButton.setText("Стать волонтером");
        becomeVolunteerButton.setCallbackData("becomeVolunteer");
        row2.add(becomeVolunteerButton);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton reportButton = new InlineKeyboardButton();
        reportButton.setText("Прислать отчет");
        reportButton.setCallbackData("report");
        row2.add(reportButton);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
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
        message.setText("Выберете раздел:");

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton aboutUsButton = new InlineKeyboardButton();
        aboutUsButton.setText("О нас");
        aboutUsButton.setCallbackData("aboutUs");
        row1.add(aboutUsButton);

        InlineKeyboardButton adoptButton = new InlineKeyboardButton();
        adoptButton.setText("Приютить животное");
        adoptButton.setCallbackData("adopt");
        row1.add(adoptButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData("callVolunteer");
        row2.add(callVolunteerButton);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("backToShelterSelection");
        row3.add(backButton);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        markupKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(markupKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            // Обработайте исключение, если не удается отправить сообщение
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
            user.getRegisteredAt();

            userRepository.save(user);
//            log.info("User saved: " + user);
        }
    }



    private void handleUserChoice(long chatId, String messageText, String shelterChoice) {
        // Обработку выбора пользователя в зависимости от приюта
        // Логика для каждого варианта
    }

    private String getShelterInfo(String shelterChoice) {
        if ("catShelter".equals(shelterChoice)) {
            return "Информация о приюте для кошек...";
        } else if ("dogShelter".equals(shelterChoice)) {
            return "Информация о приюте для собак...";
        } else {
            return "Неизвестный выбор приюта.";
        }
    }
    private void sendShelterInfo(long chatId, String shelterChoice) {
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//
//        String shelterInfo = getShelterInfo(shelterChoice);
//        message.setText(shelterInfo);
//
//        // Создаем разметку для кнопок
//        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//
//        List<InlineKeyboardButton> row1 = new ArrayList<>();
//        InlineKeyboardButton addressButton = new InlineKeyboardButton();
//        addressButton.setText(ADDRESS.getDescription());
//        addressButton.setCallbackData(ADDRESS.getCommand());
//        row1.add(addressButton);
//
//        List<InlineKeyboardButton> row2 = new ArrayList<>();
//        InlineKeyboardButton directionsButton = new InlineKeyboardButton();
//        directionsButton.setText("Схема проезда до приюта");
//        directionsButton.setCallbackData("shelterDirections");
//        row2.add(directionsButton);
//
//        List<InlineKeyboardButton> row3 = new ArrayList<>();
//        InlineKeyboardButton scheduleButton = new InlineKeyboardButton();
//        scheduleButton.setText("Расписание работы приюта");
//        scheduleButton.setCallbackData("shelterSchedule");
//        row3.add(scheduleButton);
//
//        List<InlineKeyboardButton> row4 = new ArrayList<>();
//        InlineKeyboardButton contactButton = new InlineKeyboardButton();
//        contactButton.setText("Контактные данные охраны для оформления пропуска");
//        contactButton.setCallbackData("shelterContact");
//        row4.add(contactButton);
//
//        List<InlineKeyboardButton> row5 = new ArrayList<>();
//        InlineKeyboardButton safetyButton = new InlineKeyboardButton();
//        safetyButton.setText("Рекомендации о технике безопасности на территории приюта для посетителей");
//        safetyButton.setCallbackData("shelterSafety");
//        row5.add(safetyButton);
//
//        List<InlineKeyboardButton> row6 = new ArrayList<>();
//        InlineKeyboardButton feedbackButton = new InlineKeyboardButton();
//        feedbackButton.setText("Оставить контактные данные для обратной связи");
//        feedbackButton.setCallbackData("shelterFeedback");
//        row6.add(feedbackButton);
//
//        List<InlineKeyboardButton> row7 = new ArrayList<>();
//        InlineKeyboardButton volunteerButton = new InlineKeyboardButton();
//        volunteerButton.setText(VOLUNTEER.getDescription());
//        volunteerButton.setCallbackData(VOLUNTEER.getCommand());
//        row7.add(volunteerButton);
//
//        keyboard.add(row1);
//        keyboard.add(row2);
//        keyboard.add(row3);
//        keyboard.add(row4);
//        keyboard.add(row5);
//        keyboard.add(row6);
//        keyboard.add(row7);
//        markupKeyboard.setKeyboard(keyboard);
//        message.setReplyMarkup(markupKeyboard);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            //
//        }
    }

    private void sendHowToAdoptInfoForShelter(long chatId, String shelterChoice) {
        //String shelterChoice = userShelterChoiceMap.get(chatId);
        SendMessage message = new SendMessage();

        if ("catShelter".equals(shelterChoice)) {

            message.setChatId(String.valueOf(chatId));

            String howToAdoptInfo = "Информация о том, как взять кошку из приюта...";
            message.setText(howToAdoptInfo);

        } else if ("dogShelter".equals(shelterChoice)) {

            message.setChatId(String.valueOf(chatId));

            String howToAdoptInfo = "Информация о том, как взять собаку из приюта...";
            message.setText(howToAdoptInfo);
        }

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton ruleButton = new InlineKeyboardButton();
        ruleButton.setText("Правила знакомства с животным");
        ruleButton.setCallbackData("rule");
        row1.add(ruleButton);


        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton documentsButton = new InlineKeyboardButton();
        documentsButton.setText("Необходимые документы");
        documentsButton.setCallbackData("documents");
        row2.add(documentsButton);


        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton transportButton = new InlineKeyboardButton();
        transportButton.setText("Рекомендации при транспортировке");
        transportButton.setCallbackData("transport");
        row3.add(transportButton);



        List<InlineKeyboardButton> row4 = new ArrayList<>();

        if ("catShelter".equals(shelterChoice)) {
            InlineKeyboardButton arrangementButton = new InlineKeyboardButton();
            arrangementButton.setText("Рекомендации по обустройству дома");
            arrangementButton.setCallbackData("catHouse");
            row4.add(arrangementButton);

        } else if ("dogShelter".equals(shelterChoice)) {

            InlineKeyboardButton arrangementButton = new InlineKeyboardButton();
            arrangementButton.setText("Рекомендации по обустройству дома");
            arrangementButton.setCallbackData("dogHouse");
            row4.add(arrangementButton);
        }

        List<InlineKeyboardButton> row5 = new ArrayList<>();
        InlineKeyboardButton arrangementAdultButton = new InlineKeyboardButton();
        arrangementAdultButton.setText("Список рекомендаций по обустройству дома для взрослого животного");
        arrangementAdultButton.setCallbackData("arrangementAdult");
        row5.add(arrangementAdultButton);

        List<InlineKeyboardButton> row6 = new ArrayList<>();
        InlineKeyboardButton limitedOpportunitiesButton = new InlineKeyboardButton();
        limitedOpportunitiesButton.setText("Список рекомендаций по обустройству дома для животного с ограниченными возможностями");
        limitedOpportunitiesButton.setCallbackData("limitedOpportunities");
        row6.add(limitedOpportunitiesButton);


        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboard.add(row6);

        if ("dogShelter".equals(shelterChoice)) {
            List<InlineKeyboardButton> row7 = new ArrayList<>();
            InlineKeyboardButton dogBreederButton = new InlineKeyboardButton();
            dogBreederButton.setText("Советы кинолога по первичному общению с собакой");
            dogBreederButton.setCallbackData("dogBreeder");
            row7.add(dogBreederButton);

            List<InlineKeyboardButton> row8 = new ArrayList<>();
            InlineKeyboardButton recommendationsDogTrainersButton = new InlineKeyboardButton();
            recommendationsDogTrainersButton.setText("Рекомендации по проверенным кинологам");
            recommendationsDogTrainersButton.setCallbackData("recommendationsDogTrainers");
            row8.add(recommendationsDogTrainersButton);

            keyboard.add(row7);
            keyboard.add(row8);

        }

        markupKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(markupKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //
        }
    }

    private void sendReportInstructions(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(SEND_REPORT);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //
        }
    }

    private void aboutUs(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберете раздел:");

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton aboutUsButton = new InlineKeyboardButton();
        aboutUsButton.setText("О нас");
        aboutUsButton.setCallbackData("info");
        row1.add(aboutUsButton);

        InlineKeyboardButton addressButton = new InlineKeyboardButton();
        addressButton.setText("Адрес");
        addressButton.setCallbackData("address");
        row1.add(addressButton);

        InlineKeyboardButton directionsButton = new InlineKeyboardButton();
        directionsButton.setText("Схема проезда");
        directionsButton.setCallbackData("directions");
        row1.add(directionsButton);

        InlineKeyboardButton scheduleButton = new InlineKeyboardButton();
        scheduleButton.setText("Расписание работы");
        scheduleButton.setCallbackData("schedule");
        row1.add(scheduleButton);

        keyboard.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton contactButton = new InlineKeyboardButton();
        contactButton.setText("Контактные данные охраны");
        contactButton.setCallbackData("contact");
        row2.add(contactButton);

        InlineKeyboardButton safetyButton = new InlineKeyboardButton();
        safetyButton.setText("Техника безопасности на территории");
        safetyButton.setCallbackData("safety");
        row2.add(safetyButton);

        InlineKeyboardButton feedbackButton = new InlineKeyboardButton();
        feedbackButton.setText("Обратная связь");
        feedbackButton.setCallbackData("feedback");
        row2.add(feedbackButton);

        keyboard.add(row2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton();
        volunteerButton.setText("Позвать волонтера");
        volunteerButton.setCallbackData("volunteer");
        row3.add(volunteerButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("backToShelterSelection");
        row3.add(backButton);

        keyboard.add(row3);

        markupKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(markupKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //
        }
    }

    private String getReportInstructions(String shelterChoice) {
        return "Инструкции по отправке отчета...";
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

    private void sendBecomeVolunteerInstructions(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(LEAVE_CONTACT_DETAILS);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //
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

