package ru.firstvalery.boiler.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.firstvalery.boiler.model.entity.TelegramUser;

import java.util.Optional;
import java.util.UUID;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, UUID> {
    Optional<TelegramUser> findByChatId(String chatId);
}
