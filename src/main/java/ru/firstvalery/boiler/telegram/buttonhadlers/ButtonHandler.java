package ru.firstvalery.boiler.telegram.buttonhadlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.firstvalery.boiler.model.entity.AccessLevelValues;
import ru.firstvalery.boiler.telegram.TelegramService;

public interface ButtonHandler {
    void setSender(TelegramService telegramService);

    void handle(Update update, AccessLevelValues accessLevel);

    String getName();
}
