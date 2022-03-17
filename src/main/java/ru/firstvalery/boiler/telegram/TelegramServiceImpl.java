package ru.firstvalery.boiler.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.firstvalery.boiler.config.BotConfig;
import ru.firstvalery.boiler.telegram.buttonhadlers.ButtonHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class TelegramServiceImpl extends TelegramLongPollingBot implements TelegramService {
    private final String botUserName;
    private final String botToken;
    private final Map<String, ButtonHandler> buttonHandlers;

    public TelegramServiceImpl(BotConfig botConfig, List<ButtonHandler> buttonHandlerList) {
        this.botUserName = botConfig.getUserName();
        this.botToken = botConfig.getToken();
        this.buttonHandlers = buttonHandlerList.stream()
                .collect(Collectors.toMap(ButtonHandler::getName, Function.identity()));
        for (ButtonHandler bh : buttonHandlerList) {
            bh.setSender(this);
        }
    }

    @Override
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Ошибка при инициализации телеграм " + getBotUsername() + " бота", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        User from;
        String message;
        String chatId = null;
        if (update.hasMessage()) {
            from = update.getMessage().getFrom();
            message = update.getMessage().hasText() ? update.getMessage().getText() : update.getMessage().getCaption();
            chatId = update.getMessage().getChatId().toString();

            try {
                sendMsg(chatId, false, "выберите интересующий раздел", MenuItems.rootItem);
            } catch (Exception e) {
                log.error("Ошибка при отправке сообщения в телеграм", e);
            }
        } else if (update.hasCallbackQuery()) {
            from = update.getCallbackQuery().getFrom();
            message = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();

            ButtonHandler buttonHandler = buttonHandlers.get(message);
            if (buttonHandler != null) {
                buttonHandler.handle(update);
                eraseButtons(update);
            } else {
                log.error("обработчик не зарегистрирован!");
            }
        }
        if (update.hasMessage() && update.getMessage().hasSticker()) {
            try {
                sendMsg(chatId, false, "Бот не принимает стикеры", null);
            } catch (Exception e) {
                log.error("Ошибка при отправке сообщения в телеграм1", e);
            }
        }
    }

    private void eraseButtons(Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        InlineKeyboardMarkup kMarkup = new InlineKeyboardMarkup();
        kMarkup.setKeyboard(List.of());
        editMessageText.setReplyMarkup(kMarkup);
        editMessageText.setText(update.getCallbackQuery().getMessage().getText());
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Ошибка редактирования кнопок Telegram", e);
        }
    }

    @Override
    public synchronized String sendMsg(String chatId, boolean forceReply, String answer, Map<String, String> buttons) throws Exception {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(answer);
        ReplyKeyboard markupKeyboard = prepareButtons(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);

        try {
            Message message = execute(sendMessage);
            return message.getMessageId().toString();
        } catch (Exception e) {
            log.error("Ошибка отправки в телеграм " + getBotUsername());
            throw new TelegramServiceException("Не удалось отпрвить сообщение", e);
        }
    }

    private ReplyKeyboard prepareButtons(Map<String, String> buttons) {
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButtonsList = prepareListOfInlineButtonsLists(buttons);
        markupKeyboard.setKeyboard(inlineButtonsList);
        return markupKeyboard;
    }

    private List<List<InlineKeyboardButton>> prepareListOfInlineButtonsLists(Map<String, String> buttons) {
        Stream<List<InlineKeyboardButton>> listStream = buttons.entrySet()
                .stream()
                .map(btn -> {
                    List<InlineKeyboardButton> inlineButtons = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(btn.getValue());
                    button.setCallbackData(btn.getKey());
                    inlineButtons.add(button);
                    return inlineButtons;
                });

        if (calculateAllButtonsRequiredLength(buttons) <= 20) {
            return List.of(listStream.flatMap(List::stream)
                    .collect(Collectors.toList()));
        } else {
            return listStream.collect(Collectors.toList());
        }
    }


    private int calculateAllButtonsRequiredLength(Map<String, String> buttons) {
        return buttons.values()
                .stream()
                .map(String::length)
                .reduce((x, y) -> x + y)
                .orElse(0);
    }


    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}
