package com.example.helper.controllers;

import com.example.helper.models.Message;
import com.example.helper.models.Role;
import com.example.helper.models.User;
import com.example.helper.service.MessageService;
import com.example.helper.service.StatusService;
import com.example.helper.service.UserService;
import com.example.helper.telegram.TelegramBot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class RegistrationController {
    private final UserService userService;
    private final MessageService messageService;

    private final StatusService statusService;

    private final TelegramBot telegramBot;

    public RegistrationController(UserService userService, MessageService messageService, StatusService statusService, TelegramBot telegramBot) {
        this.userService = userService;
        this.messageService = messageService;
        this.statusService = statusService;
        this.telegramBot = telegramBot;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (!userService.addUser(user)){
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivate = userService.activateUser(code);
        if(isActivate){
            model.addAttribute("message", "User successfully activated");
        } else{
            model.addAttribute("message", "Activation code is not found");
        }
        return "login";
    }
}