package ru.firstvalery.boiler.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "telegram")
@Getter
@Setter
@NoArgsConstructor
public class Telegram {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Lob
    @Column(name = "chat_it", nullable = false)
    private String chatIt;

    @Lob
    @Column(name = "username", nullable = false)
    private String username;

    @Lob
    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "enable")
    private Boolean enable;

    @ManyToOne(optional = false)
    @JoinColumn(name = "access_level_id", nullable = false)
    private AccessLevel accessLevel;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}