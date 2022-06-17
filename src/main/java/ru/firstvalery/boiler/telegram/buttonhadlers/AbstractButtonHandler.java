package ru.firstvalery.boiler.telegram.buttonhadlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.firstvalery.boiler.model.entity.AccessLevelValues;
import ru.firstvalery.boiler.telegram.TelegramService;

public abstract class AbstractButtonHandler implements ButtonHandler {
    protected TelegramService telegramService;

    @Override
    public void setSender(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Override
    abstract public void handle(Update update, AccessLevelValues accessLevel);

    @Override
    abstract public String getName();
}
