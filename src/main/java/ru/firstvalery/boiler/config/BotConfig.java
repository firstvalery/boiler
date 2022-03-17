package ru.firstvalery.boiler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("telegram-bot")
@Getter
@Setter
public class BotConfig {
    private String userName;
    private String token;

}
