package ru.firstvalery.boiler.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "telegram_user")
@Getter
@Setter
@NoArgsConstructor
public class TelegramUser {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "chat_id", nullable = false)
    private String chatId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "retry", nullable = false)
    private int retry;

    @ManyToOne(optional = false)
    @JoinColumn(name = "access_level_id", nullable = false)
    private AccessLevel accessLevel;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Long createdAt;
}