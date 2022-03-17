package ru.firstvalery.boiler.telegram.buttonhadlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.firstvalery.boiler.sensors.SensorService;
import ru.firstvalery.boiler.telegram.MenuItems;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ControlButtonHandler extends AbstractButtonHandler {
    private final SensorService sensorService;

    public ControlButtonHandler(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public void handle(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        try {
            telegramService.sendMsg(chatId, false, "управление ТЭН", createTenButtons());
        } catch (Exception e) {
            log.error("Ошибка отправки бота");
        }
    }

    private Map<String, String> createTenButtons() {
        Map<String, String> buttonsMap = sensorService.getHeatingElements()
                .stream()
                .collect(Collectors.toMap(
                        heatingElement -> {
                            if (heatingElement.isOn()) {
                                return "turnOff" + heatingElement.getName();
                            } else {
                                return "turnOn" + heatingElement.getName();
                            }
                        }, heatingElement -> {
                            if (heatingElement.isOn()) {
                                return "отключить " + heatingElement.getName();
                            } else {
                                return "включить " + heatingElement.getName();
                            }
                        }
                ));
        buttonsMap.putAll(MenuItems.returnToRootItem);
        return buttonsMap;
    }

    @Override
    public String getName() {
        return "control";
    }
}
