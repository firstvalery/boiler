package ru.firstvalery.boiler.telegram;

import ru.firstvalery.boiler.init.Initializable;

import java.util.Map;

public interface TelegramService extends Initializable {
    String sendMsg(String chatId, boolean forceReply, String answer, Map<String, String> buttons) throws Exception;
}
