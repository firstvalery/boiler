package ru.firstvalery.boiler.telegram.buttonhadlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.firstvalery.boiler.telegram.MenuItems;

@Slf4j
@Component
public class RootButtonHandler extends AbstractButtonHandler {
    @Override
    public void handle(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        try {
            telegramService.sendMsg(chatId, false, "выберите интересующий раздел", MenuItems.rootItem);
        } catch (Exception e) {
            log.error("ошибка отправки бота");
        }
    }

    @Override
    public String getName() {
        return "root";
    }
}
