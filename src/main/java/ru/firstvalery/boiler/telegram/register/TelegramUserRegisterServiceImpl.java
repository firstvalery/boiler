package ru.firstvalery.boiler.telegram.register;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.firstvalery.boiler.config.BotConfig;
import ru.firstvalery.boiler.model.entity.AccessLevel;
import ru.firstvalery.boiler.model.entity.AccessLevelValues;
import ru.firstvalery.boiler.model.entity.TelegramUser;
import ru.firstvalery.boiler.model.repository.AccessLevelRepository;
import ru.firstvalery.boiler.model.repository.TelegramUserRepository;
import ru.firstvalery.boiler.telegram.TelegramService;
import ru.firstvalery.boiler.telegram.TelegramServiceException;

import java.util.Map;
import java.util.UUID;

@Service
public class TelegramUserRegisterServiceImpl implements TelegramUserRegisterService {
    private final TelegramUserRepository telegramUserRepository;
    private final AccessLevelRepository accessLevelRepository;
    private final String monitoringLevelCode;
    private final String fullControlCode;

    public TelegramUserRegisterServiceImpl(TelegramUserRepository telegramUserRepository,
                                           AccessLevelRepository accessLevelRepository,
                                           BotConfig botConfig) {
        this.telegramUserRepository = telegramUserRepository;
        this.accessLevelRepository = accessLevelRepository;
        this.monitoringLevelCode = botConfig.getSecretCodeMonitoring();
        this.fullControlCode = botConfig.getSecretCodeFullControl();
    }

    @Override
    @Transactional
    public TelegramUser checkUserRegistration(String chatId, String message, User from, TelegramService telegramService) {
        TelegramUser telegramUser = telegramUserRepository.findByChatId(chatId).orElse(new TelegramUser());

        if (telegramUser.getId() == null) {
            telegramUser.setId(UUID.randomUUID());
            telegramUser.setChatId(chatId);
            telegramUser.setUsername(from.getUserName());
            telegramUser.setRetry(0);
            AccessLevel accesslevel = accessLevelRepository.findByCode(AccessLevelValues.ACCESS_DENIED.name())
                    .orElseThrow(() -> new TelegramServiceException("Объект не найден в базе "));
            telegramUser.setAccessLevel(accesslevel);

            telegramUserRepository.save(telegramUser);
            telegramService.sendMsg(chatId, false, "введите код подтверждения", Map.of());

        } else if (isAccessLevelAnAccessDenied(telegramUser)) {
            if (telegramUser.getRetry() >= 3) {

                telegramService.sendMsg(chatId, false, "вы отключены от сервиса", Map.of());

            } else {
                if (monitoringLevelCode.equals(message)) {

                    telegramUser.setAccessLevel(findAccessLevelByCode(AccessLevelValues.MONITORING_ONLY));
                    telegramUser.setRetry(0);
                    telegramUserRepository.save(telegramUser);

                } else if (fullControlCode.equals(message)) {

                    telegramUser.setAccessLevel(findAccessLevelByCode(AccessLevelValues.FULL_CONTROL));
                    telegramUser.setRetry(0);
                    telegramUserRepository.save(telegramUser);

                } else {
                    telegramUser.setRetry(telegramUser.getRetry() + 1);
                    telegramUserRepository.save(telegramUser);
                    telegramService.sendMsg(chatId, false, "введите код подтверждения", Map.of());
                }
            }
        }
        return telegramUser;
    }

    private boolean isAccessLevelAnAccessDenied(TelegramUser telegramUser) {
        return AccessLevelValues.ACCESS_DENIED.name().equals(telegramUser.getAccessLevel().getCode());
    }

    private boolean isAccessLevelAnMonitoringOnly(TelegramUser telegramUser) {
        return AccessLevelValues.MONITORING_ONLY.name().equals(telegramUser.getAccessLevel().getCode());
    }

    private AccessLevel findAccessLevelByCode(AccessLevelValues accessLevelValues) {
        return accessLevelRepository.findByCode(accessLevelValues.name())
                .orElseThrow(() -> new TelegramServiceException("Объект не найден"));
    }


}
