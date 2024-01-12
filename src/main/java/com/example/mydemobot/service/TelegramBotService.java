package com.example.mydemobot.service;

import com.example.mydemobot.config.BotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    private final BotProperties botProperties;

    @Autowired
    public TelegramBotService(String botToken, BotProperties botProperties) {
        super(botToken);
        this.botProperties = botProperties;
        try {
            execute(
                    new SetMyCommands(
                            botProperties.getCommandList(),
                            new BotCommandScopeDefault(),
                            null
                    )
            );
        } catch (TelegramApiException e) {
            log.error("Error setting the command list ${}",e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            log.info("Message received: ${}", update.getMessage().getText());
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start" -> {
                    sendMessage(chatId, "Hello, " + update.getMessage().getChat().getFirstName());
                }
                default -> {
                    sendMessage(chatId, "Command not supported");
                }
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            execute(sendMessage);
            log.info("Sent message: ${}", sendMessage.getText() );
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getBotName();
    }
}
