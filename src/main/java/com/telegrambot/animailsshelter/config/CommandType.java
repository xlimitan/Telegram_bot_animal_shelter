package com.telegrambot.animailsshelter.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandType {
    CATS("/cats", "Приют для кошек"),
    DOGS("/dogs", "Приют для собак"),
    START("/start", "Добро пожаловать!"),
    INFO("/info", "Информация о приюте"),
    REPORT("/report", "Прислать отчет о питомце"),
    VOLUNTEER("/volunteer", "Позвать волонтера"),
    SHELTER("/shelter", "О приюте"),
    CONTACT("/contact", "Телефон приюта"),
    PHONE("/phone", "Оставить номер телефона для связи");
    private final String command;
    private final String description;
}
