package com.telegrambot.animailsshelter.service;

import com.telegrambot.animailsshelter.config.BotConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    static final String CAT_SHELTER = "Это приют для кошек.\n\n" +
            "Выберете интиресующий вас пункт.\n\n"+
            "Нажмите /infoShelter для подробной информации о приюте.\n\n" +
            "Нажмите /takeAnimals если хотите приютить кошек.\n\n" +
            "Нажмите /sendReport если хотите отправить отчет о кошки.\n\n" +
            "Нажмите /callVolunteer если нужна помощ волонтера.\n\n";
    static final String DOG_SHELTER = "Это приют для собак.\n\n" +
            "Выберете интиресующий вас пункт.\n\n"+
            "Нажмите /infoShelter для подробной информации о приюте.\n\n" +
            "Нажмите /takeAnimals если хотите приютить собаку.\n\n" +
            "Нажмите /sendReport если хотите отправить отчет о собаке.\n\n" +
            "Нажмите /callVolunteer если нужна помощ волонтера.\n\n";

    static final String HELP_TEXT = "This bot is created  to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu on the left or by typing a command:\n\n" +
            "Type /start to see welcome message\n\n" +
            "Type /mydata to see date stored about yourself\n\n" +
            "Type /help to see this message again";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/deletedata", "delete my data"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        listOfCommands.add(new BotCommand("/settings", "set your preferences"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String massageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (massageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    sendMessage(chatId,"Нажмите /dogShelter для получения информации по приюту для собак.\n\n" +
                            "Нажмите /catShelter для получения информации по приюту для кошек.\n\n");
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                case "/dogShelter":
                    sendMessage(chatId, DOG_SHELTER);
                    break;
                case "/catShelter":
                    sendMessage(chatId, CAT_SHELTER);
                    break;
                default: sendMessage(chatId, "Извините, неизвестная команда");
            }
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

    private void startCommandReceived(long chatId, String name) {

        String answer = "Приветствуем " + name + "!";
            log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
