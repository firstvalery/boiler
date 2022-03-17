package ru.firstvalery.boiler.telegram;

public class TelegramServiceException extends RuntimeException {

    public TelegramServiceException(String message) {
        super(message);
    }

    public TelegramServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
