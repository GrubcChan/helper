package com.example.helper.controllers;

import com.example.helper.models.Role;
import com.example.helper.models.User;
import com.example.helper.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("is_invalid", false);
//        model.addAttribute("user", new User());
        return "users/index";
    }

    @GetMapping("/users/{id}")
    public String show(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
        model.addAttribute("user_i", userService.findById(id));
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "users/show";
    }

    @PostMapping("/users")
    public String create(@Valid User user,
                         BindingResult bindingResult,
                         Model model
    ) {
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            model.addAttribute("is_invalid", true);
        } else{
            if (!userService.addUser(user)) {
                model.addAttribute("message", "User exists!");
            }
            model.addAttribute("is_invalid", false);
//            model.addAttribute("user", new User());
        }

        model.addAttribute("users", userService.findAll());

        return "users/index";
    }

    @PatchMapping("/users/{id}")
    public String update(
            @PathVariable("id") Long id,
            @RequestParam Map<String, String> form,

            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String chat_id
    ) {
        userService.updateUser(id, form, username, password, chat_id);

        return "redirect:/users/" + id;
    }

    @DeleteMapping("/users/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}
