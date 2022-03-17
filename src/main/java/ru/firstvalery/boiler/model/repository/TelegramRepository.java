package ru.firstvalery.boiler.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.firstvalery.boiler.model.entity.Telegram;

import java.util.UUID;

public interface TelegramRepository extends JpaRepository<Telegram, UUID> {
}
