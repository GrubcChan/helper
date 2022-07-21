package com.example.helper;

import com.example.helper.telegram.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@SpringBootApplication
public class HelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelperApplication.class, args);

//        try{
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            botsApi.registerBot(new TelegramBot());
//        } catch (TelegramApiException e){
//            e.printStackTrace();
//        }

    }
}
