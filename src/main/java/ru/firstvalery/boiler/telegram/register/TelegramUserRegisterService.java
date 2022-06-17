package ru.firstvalery.boiler.telegram.register;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.firstvalery.boiler.model.entity.TelegramUser;
import ru.firstvalery.boiler.telegram.TelegramService;

public interface TelegramUserRegisterService {

    TelegramUser checkUserRegistration(String chatId, String message, User from, TelegramService telegramService);
}
