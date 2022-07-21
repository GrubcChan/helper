package com.example.helper.controllers;

import com.example.helper.models.User;
import com.example.helper.service.ControlService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping()
    public String home(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "home";
    }
}