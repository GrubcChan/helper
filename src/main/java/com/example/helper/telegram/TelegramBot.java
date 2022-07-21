package com.example.helper.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

//    public String chat_id = "1959922693";
    public TelegramBot() {}

    public void sendMessage(String chat_id, String text){
        try {
            SendMessage message = new SendMessage();
            message.setChatId(chat_id);
            message.setText(text);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        String botUserName = "HelpDeskTextBot";
        return botUserName;
    }

    @Override
    public String getBotToken() {
        String botToken = "5386234880:AAFi6lJLUM15m3z0sigi_xs5DuQXSfT4F2I";
        return botToken;
    }
}
