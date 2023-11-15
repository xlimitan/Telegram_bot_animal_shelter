package com.telegrambot.animailsshelter.service;

import ch.qos.logback.classic.Logger;
import com.telegrambot.animailsshelter.config.BotConfig;
import com.telegrambot.animailsshelter.model.AnimalOwner;
import com.telegrambot.animailsshelter.model.PetReport;
import com.telegrambot.animailsshelter.model.PhotoReport;
import com.telegrambot.animailsshelter.model.User;
import com.telegrambot.animailsshelter.repository.PetReportRepository;
import com.telegrambot.animailsshelter.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.logging.ErrorManager;

import static com.telegrambot.animailsshelter.config.Information.*;


/**
 * Класс TelegramBot - это основной класс Telegram-бота, который обрабатывает входящие обновления и сообщения.
 * Он предоставляет интерфейс для взаимодействия с пользователем и управления базой данных.
 */
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
@Autowired
    private UserRepository userRepository;
    private final Map<Long, String> userShelterChoiceMap;
    private final UserService userService;
    private final ReportService reportService;
    private final AddService addService;
    private final PetReportRepository petReportRepository;
    private final PhotoReportService photoReportService;

    public TelegramBot(BotConfig config, UserService userService, ReportService reportService, AddService addService, PetReportRepository petReportRepository, PhotoReportService photoReportService) {
        this.config = config;
        this.userService = userService;
        this.reportService = reportService;
        this.addService = addService;
        this.petReportRepository = petReportRepository;
        this.photoReportService = photoReportService;
        this.userShelterChoiceMap = new HashMap<>();
    }


    /**
     * Обработчик входящих обновлений и сообщений от пользователей. Вызывается при получении нового обновления.
     *
     * @param update Объект, содержащий информацию об обновлении.
     */

    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
            Message message = update.getMessage();
            Long chatId;
            if (update.getMessage() != null) {
                chatId = update.getMessage().getChatId();
            } else {
                chatId = update.getCallbackQuery().getMessage().getChatId();
            }
            User user = userRepository.findById(chatId).orElse(null);

            if (message.getText().startsWith("+7")) {
                userService.savePhoneUser(message.getChatId(), message.getText());
                sendText(message.getChatId(), "Номер сохранён!");
            }
            if (message.getText().startsWith("Отчёт")) {
                addService.petReportSave(user, message.getText());
                sendText(message.getChatId(), "Отчёт сохранён");
            }
        }else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
        //asd adasdasdasd
        Long id;
        if (update.getMessage() != null) {
            id = update.getMessage().getChatId();
        } else {
            id = update.getCallbackQuery().getMessage().getChatId();
        }
//            long id = update.getMessage().getChatId();
            if (update.hasMessage() && update.getMessage().hasPhoto()) {
                PhotoSize photo = update.getMessage().getPhoto().get(3);
                GetFile getFile = new GetFile(photo.getFileId());
                var file = execute(getFile);
                File path = new java.io.File("photos/" + id + "_" + photo.getFileUniqueId() + LocalDate.now() + ".jpg");
                downloadFile(file, path);
                // если сегодня фото уже присылалось, перезаписываем путь в БД (один день одно фото)
                if (photoReportService.findPhotoReportByUserIdAndDate(id, LocalDate.now()) == null) {
                    try {
                        PhotoReport photoReport = new PhotoReport(id, LocalDate.now(), path.getPath());
                        photoReportService.addPhotoReport(photoReport);
                    } catch (Exception e) {
                    }
                } else {
                    photoReportService.recordDirPhoto(id, path.getPath());
                }
            }
    }
    @Scheduled(fixedDelay = 5000)
public  void checkFinalDate(){
    List<User> users = userService.getAllUsers();                                                          // список всех владельцев
    for (User user : users) {
        if (!user.isTrialPeriod()) {
            if (user.getDate().equals(LocalDate.now().plusDays(14))) {
                sendText(user.getChatId(), "Вам назначен дополнительный испытательный срок 14 дней");
            }
             if (user.getDate().equals(LocalDate.now().plusDays(30))) {
                sendText(user.getChatId(), "Вам назначен дополнительный испытательный срок 30 дней");
            }
        } else  {
            if (user.isTrialPeriod()) {
                sendText(user.getChatId(), "Поздравляем, вы прошли испытательный срок");
                }
                sendText(user.getChatId(), "Вы не прошли испытательный срок");
            }
        }
    }


//    @Scheduled(cron = "* * 21 * * *")
    @Scheduled(fixedDelay = 5000)
    public void checkingReports() {
        log.error("Сообщение не отправлено!");
        log.info("Сообщение не отправлено!");
        LocalDate dateNow = LocalDate.now();
        List<User> users = userService.getAllUsers();                                                          // список всех владельцев
        for (User user : users) {
            if (petReportRepository.findByUser_ChatIdAndDate(user.getChatId(), dateNow) == null) {                               // если сегодня отчетов не было
                sendText(user.getChatId(), "Отправьте отчет"); // !!! проверить строку
            }
            if (petReportRepository.checkingLastDateReports(user.getChatId()) == null) {                                              // если владелец еще не оставлял отчетов
                Period period = user.getDate().until(dateNow);                                                            // проверяем как давно он взял питомца
                if (period.getDays() >= 2) {                                                                                     // если больше двух дней назад
                    sendText(user.getChatId(), "Отчётов не было больше двух дней. Информация переданная волонтеру!");
//                    sendText(petReportRepository.findByUser_ChatIdAndDate(user.getChatId(),dateNow).getId(), "Отчётов не было больше двух дней. Информация переданная волонтеру!");
                }
            } else {
                PetReport petReport = petReportRepository.checkingLastDateReports(user.getChatId());                                 // если отчет есть, забираем по последней дате добавления
                LocalDate reportDate = LocalDate.from(petReport.getDate());                                                                     // извлекаем дату
                if (!(petReport.getDate().equals(dateNow))) {                                                                    // сверяем с текущей датой, ни сегодня ли прислан отчет
                    Period period = reportDate.until(dateNow);                                                                  // если нет проверяем как давно
                    if (period.getDays() >= 2) {                                                                                 // если два или более дня назад
                        sendText(user.getChatId(), "Отчётов не было больше двух дней. Информация переданная волонтеру!");
//                        sendText(petReportRepository.findByUser_ChatIdAndDate(user.getChatId(),dateNow).getId(), "Отчётов не было больше двух дней. Информация переданная волонтеру!");
                    }
                } else {
                    if (petReport.getReport() == null) {
                        sendText(user.getChatId(), "Пришлите текстовый отчет");
//                        sendText(petReportRepository.findByUser_ChatIdAndDate(user.getChatId(),dateNow).getId(), "Пришлите текстовый отчет");
                    }
                    if (photoReportService.findPhotoReportByUserIdAndDate(user.getChatId(), LocalDate.now()) == null) {
                        sendText(user.getChatId(), "Пришлите фотографию животного");
//                        sendText(petReportRepository.findByUser_ChatIdAndDate(user.getChatId(),dateNow).getId(), "Пришлите фотографию животного");
                    }
                }
            }
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
                case "report" -> {
                    sendReportInstructions(chatId);
                    checkingReports();
                }
                case "volunteer" -> sendText(chatId, CALL_VOLUNTEER);
                case "aboutUs" -> aboutUs(chatId);
                case "address" -> sendText(chatId, ADDRESS_SHELTER);
                case "directions" -> sendText(chatId, DRIVING_DIRECTIONS);
                case "schedule" -> sendText(chatId, WORK_SCHEDULE);
                case "contact" -> sendText(chatId, SECURITY_DATA);
                case "safety" -> sendText(chatId, SAFETY_TECHNICAL);
                case "feedback" -> sendText(chatId, LEAVE_CONTACT_DETAILS);
                case "rule" -> {sendText(chatId, RULES_OF_MEETING_A_PET);
                    sendHowToAdoptInfoForShelter(chatId,userShelterChoiceMap.get(chatId));
                   }
                case "documents" -> sendText(chatId, REQUIRED_DOCUMENTS);
                case "transport" -> sendText(chatId, RECOMMENDATIONS_TRANSPORTATION);
                case "homeImprovement" -> homeImprovement(chatId, userShelterChoiceMap.get(chatId));
                case "puppyRecommendations" -> sendText(chatId, RECOMMENDATIONS_ADVANCEMENT_YOUNG);
                case "oldRecommendations" -> sendText(chatId, RECOMMENDATIONS_ADVANCEMENT_OLD);
                case "disabledRecommendations" -> sendText(chatId, RECOMMENDATIONS_ADVANCEMENT_DISABLED);
                case "cynologist" -> cynologist(chatId);
                case "reasonsRefusal" -> sendText(chatId, REASONS_FAILURE);
                case "tipsCynologist" -> sendText(chatId, TIPS_KINOLOGIST);
                case "choiceCynologist" -> sendText(chatId, CHOICE_KINOLOGIST);
                case "backToShelterSelection" -> {
                    userShelterChoiceMap.remove(chatId);
                    sendWelcomeMessage(chatId);
                }
                case "back" -> {
                    sendMenuOptions(chatId);
                }
                default -> {
                    if (Arrays.asList("catShelter", "dogShelter").contains(callbackData)) {
                        sendErrorMessageAboutChoosingShelter(chatId);
                    } else {
                        sendErrorMessage(chatId);
                    }
                }
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
                case "back" -> {
                    sendWelcomeMessage(chatId);
                }
                case "report" -> sendReportInstructions(chatId);
                default -> sendErrorMessageAboutChoosingShelter(chatId);
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

    private void sendErrorMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Ошибка");

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("back");
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
     * Отправляет сообщение об ошибке пользователю.
     *
     * @param chatId Идентификатор чата, куда отправить сообщение.
     */
    private void sendErrorMessageAboutChoosingShelter(long chatId) {
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
            //
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
            //
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
            user.setPhoneNumber(user.getPhoneNumber());
            user.seteMail(user.geteMail());

            userRepository.save(user);
//            log.info("User saved: " + user);
        }
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

            arrangementButton.setCallbackData("homeImprovement");

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
            InlineKeyboardButton cynologistButton = new InlineKeyboardButton();
            cynologistButton.setText("Кинологи");
            cynologistButton.setCallbackData("cynologist");
            row7.add(cynologistButton);

            List<InlineKeyboardButton> row8 = new ArrayList<>();
            InlineKeyboardButton reasonsRefusalButton = new InlineKeyboardButton();
            reasonsRefusalButton.setText("Причины отказа");
            reasonsRefusalButton.setCallbackData("reasonsRefusal");
            row8.add(reasonsRefusalButton);

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


    private void homeImprovement(long chatId, String shelterChoice) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберете раздел:");

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        if ("catShelter".equals(shelterChoice)) {
            InlineKeyboardButton puppyButton = new InlineKeyboardButton();
            puppyButton.setText("Котенок");
            puppyButton.setCallbackData("puppyRecommendations");
            row1.add(puppyButton);

        } else if ("dogShelter".equals(shelterChoice)) {
            InlineKeyboardButton puppyButton = new InlineKeyboardButton();
            puppyButton.setText("Щенок");
            puppyButton.setCallbackData("puppyRecommendations");
            row1.add(puppyButton);
        }


        InlineKeyboardButton adultButton = new InlineKeyboardButton();
        adultButton.setText("Взрослое животное");
        adultButton.setCallbackData("oldRecommendations");
        row1.add(adultButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton disabledButton = new InlineKeyboardButton();
        disabledButton.setText("Животное с ограниченными возможностями");
        disabledButton.setCallbackData("disabledRecommendations");
        row2.add(disabledButton);

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
            //
        }
    }

    private void cynologist(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберете раздел:");

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton tipsCynologistButton = new InlineKeyboardButton();
        tipsCynologistButton.setText("Советы кинологов");
        tipsCynologistButton.setCallbackData("tipsCynologist");
        row1.add(tipsCynologistButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton choiceCynologistButton = new InlineKeyboardButton();
        choiceCynologistButton.setText("Рекомендуемые кинологи");
        choiceCynologistButton.setCallbackData("choiceCynologist");
        row2.add(choiceCynologistButton);


        keyboard.add(row1);
        keyboard.add(row2);
        markupKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(markupKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //
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

