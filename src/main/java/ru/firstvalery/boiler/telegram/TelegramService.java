package ru.firstvalery.boiler.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.firstvalery.boiler.init.Initializable;

import java.util.Map;

public interface TelegramService extends Initializable {
    String sendMsg(String chatId, boolean forceReply, String answer, Map<String, String> buttons);

    void removeButtons(Update update);
}
