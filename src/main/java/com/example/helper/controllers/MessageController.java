package com.example.helper.controllers;


import com.example.helper.models.Message;
import com.example.helper.models.Role;
import com.example.helper.models.User;
import com.example.helper.service.MessageService;
import com.example.helper.service.StatusService;
import com.example.helper.service.UserService;
import com.example.helper.telegram.TelegramBot;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;


@Controller
public class MessageController {
    private final MessageService messageService;

    private final StatusService statusService;

    private final UserService userService;

    private final TelegramBot telegramBot;

    public MessageController(MessageService messageService, StatusService statusService, UserService userService, TelegramBot telegramBot) {
        this.messageService = messageService;
        this.statusService = statusService;
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    @GetMapping("/messages")
    public String index(@AuthenticationPrincipal User user, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam(required = false, defaultValue = "") String status, Model model) {

        model.addAttribute("user", user);
        model.addAttribute("filter", filter);
        model.addAttribute("status", status);

        model.addAttribute("messages", messageService.index(filter, status, statusService.findByName(status)));
        model.addAttribute("statuses", statusService.findAll());

        model.addAttribute("is_invalid", false);
        model.addAttribute("this_time", new Timestamp(System.currentTimeMillis()));

        return "messages/index";
    }

    @GetMapping("/messages/{id}")
    public String show(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
        model.addAttribute("user", user);

        model.addAttribute("message", messageService.findById(id));
        model.addAttribute("statuses", statusService.findAll());
        return "messages/show";
    }

    @PostMapping("/messages")
    public String create(@AuthenticationPrincipal User user,

                         @RequestParam(required = false, defaultValue = "") String filter,
                         @RequestParam(required = false, defaultValue = "") String status,

                         @Valid Message message,
                         BindingResult bindingResult,
                         Model model,

                         @RequestParam String status_i
    ) {
        if(bindingResult.hasErrors()){
            model.addAttribute("message", message);
            model.addAttribute("is_invalid", true);
        } else{
            messageService.addMessage(message.getTag(), message.getText(), statusService.findByName(status_i), user);
            model.addAttribute("is_invalid", false);
        }

        model.addAttribute("user", user);
        model.addAttribute("filter", filter);
        model.addAttribute("status", status);

        model.addAttribute("messages", messageService.index(filter, status, statusService.findByName(status)));
        model.addAttribute("statuses", statusService.findAll());

        model.addAttribute("this_time", new Timestamp(System.currentTimeMillis()));

        return "messages/index";
    }

    @PatchMapping("/messages/{id}")
    public String update(@AuthenticationPrincipal User user,

                         @RequestParam String tag,
                         @RequestParam String text,
                         @RequestParam String status,
                         @PathVariable("id") Long id,
                         Model model
    ) {
        Message message_old = messageService.findById(id);
        if(!messageService.updateMessage(tag, text, statusService.findByName(status), user, id)){
            Message message = messageService.findById(id);

            String mes = String.format(
                    "The status of the request you created has changed from %s to %s!",
                    message_old.getStatus().getName(),
                    status
            );

            telegramBot.sendMessage(message.getAuthor().getChat_id(), mes);
        }

        return "redirect:/messages/" + id;
    }

    @PatchMapping("/messages/status/{id}")
    public String updateStatus(
                         @RequestParam String status,
                         @PathVariable("id") Long id
    ) {
        Message message_old = messageService.findById(id);
        if(!messageService.updateStatus(statusService.findByName(status), id)){
            Message message = messageService.findById(id);

            String mes = String.format(
                    "The status of the request you created has changed from %s to %s!",
                    message_old.getStatus().getName(),
                    status
            );

            telegramBot.sendMessage(message.getAuthor().getChat_id(), mes);
        }

        return "redirect:/messages";
    }

    @DeleteMapping("/messages/{id}")
    public String delete(@PathVariable("id") Long id) {
        messageService.deleteById(id);
        return "redirect:/messages";
    }
}
