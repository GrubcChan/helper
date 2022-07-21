package com.example.helper.controllers;

import com.example.helper.models.User;
import com.example.helper.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PersonalAreaController {

    private final UserService userService;

    public PersonalAreaController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/personal_area")
    public String personal(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "personal_area";
    }

    @PatchMapping("/personal_area")
    public String update(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String chat_id,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        userService.updatePersonalArea(user, username, password, chat_id);

        return "redirect:/personal_area";
    }

    @DeleteMapping("/personal_area")
    public String delete(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        userService.deleteById(user.getId());
        return "redirect:/personal_area";
    }
}
