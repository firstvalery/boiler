package ru.firstvalery.boiler.telegram.buttonhadlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.firstvalery.boiler.model.entity.AccessLevelValues;
import ru.firstvalery.boiler.sensors.AnalogSensor;
import ru.firstvalery.boiler.sensors.SensorService;
import ru.firstvalery.boiler.telegram.MenuItems;

@Component
@Slf4j
public class MonitoringButtonHandler extends AbstractButtonHandler {
    private final SensorService sensorService;
    private final String lineSeparator = System.lineSeparator();

    public MonitoringButtonHandler(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public void handle(Update update, AccessLevelValues accessLevel) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        try {
            String analogParametersView = prepareAnalogParameters();
            telegramService.sendMsg(chatId, false, "состояние устройств:" + lineSeparator + analogParametersView,
                    MenuItems.returnToRootItem);
            telegramService.removeButtons(update);
        } catch (Exception e) {
            log.error("Ошибка отправки бота");
        }
    }

    @Override
    public String getName() {
        return "monitoring";
    }

    private String prepareAnalogParameters() {
        return sensorService.getAnalogParameters()
                .stream()
                .map(AnalogSensor::toString)
                .reduce((x, y) -> x + lineSeparator + y)
                .orElse("");
    }
}
