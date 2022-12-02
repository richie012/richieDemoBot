package com.example.richieDemoBot.service;

import com.example.richieDemoBot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Clock;
import java.time.Instant;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        //проверка пришло ли сообщение и содержит ли оно текст. если да то присваеваем переменной это сообщение
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText) {
                //если получили сообщение старт делаем то то
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/time":
                    sendUtcTime(chatId);
                    break;
                default:
                    sendMessage(chatId, "Command was not supported");

            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ". nice to meet you!";
        sendMessage(chatId, answer);
    }

    private void sendUtcTime(long chatId) {
        Clock clock = Clock.systemUTC();
        Instant instant = clock.instant();
        String time = instant.toString().substring(0, 10)+"\n"+instant.toString().substring(11, 19);//2022-12-01T19:20:25.893070481Z
        sendMessage(chatId, time);

    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }
}
