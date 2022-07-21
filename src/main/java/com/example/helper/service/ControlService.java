package com.example.helper.service;

import com.example.helper.models.Message;
import com.example.helper.models.Role;
import com.example.helper.models.User;
import com.example.helper.telegram.TelegramBot;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ControlService {

    private final UserService userService;

    private final MessageService messageService;


    private final TelegramBot telegramBot;

    public ControlService(UserService userService, MessageService messageService, TelegramBot telegramBot) {
        this.userService = userService;
        this.messageService = messageService;
        this.telegramBot = telegramBot;
        control();
    }

    @Scheduled(fixedDelay=60000)
    public void control() {
        List<Message> messages = messageService.findAll();
        for (Message message : messages) {
            Timestamp this_time = new Timestamp(System.currentTimeMillis());
            if(this_time.getTime() - message.getDate_create().getTime() > message.getStatus().getReaction_time()){
                if(message.isActive()){
                    messageService.setActive(false, message.getId());

                    String mes = String.format(
                            "Hi, %s, the response time for application number %s has expired!",
                            message.getAuthor().getUsername(),
                            message.getId()
                    );

                    telegramBot.sendMessage(message.getAuthor().getChat_id(), mes);
                    List<User> users = userService.findAll();
                    for (User user_t : users) {
                        if(user_t.getRoles().contains(Role.MODERATOR)){
                            String mess = String.format(
                                    "Hi, %s, the response time for application number %s has expired!",
                                    user_t.getUsername(),
                                    message.getId()
                            );

                            telegramBot.sendMessage(user_t.getChat_id(), mess);
                        }
                    }
                }
            }
        }
    }
}
