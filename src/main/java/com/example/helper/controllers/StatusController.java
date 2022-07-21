package com.example.helper.controllers;

import com.example.helper.models.User;
import com.example.helper.service.StatusService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }


    @GetMapping("/statuses")
    public String index(@AuthenticationPrincipal User user,  Model model) {
        model.addAttribute("statuses", statusService.findAll());
        model.addAttribute("user", user);
        return "statuses/index";
    }

    @GetMapping("/statuses/{id}")
    public String show(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
        model.addAttribute("status", statusService.findById(id));
        model.addAttribute("user", user);
        return "statuses/show";
    }

    @PostMapping("/statuses")
    public String create(@RequestParam String name,
                         @RequestParam Long count_day,
                         @RequestParam Long count_hour,
                         @RequestParam Long count_minute,
                         @RequestParam Long count_cec
    ) {
        statusService.addStatus(name, count_day, count_hour, count_minute, count_cec);

        return "redirect:/statuses";
    }

    @PatchMapping("/statuses/{id}")
    public String update(@RequestParam String name,
                         @RequestParam Long count_day,
                         @RequestParam Long count_hour,
                         @RequestParam Long count_minute,
                         @RequestParam Long count_cec,
                         @PathVariable("id") Long id,
                         Model model
    ) {
        statusService.updateStatus(id, name, count_day, count_hour, count_minute, count_cec);

        return "redirect:/statuses/"+id;
    }

    @DeleteMapping("/statuses/{id}")
    public String delete(@PathVariable("id") Long id) {
        statusService.deleteById(id);
        return "redirect:/statuses";
    }
}
