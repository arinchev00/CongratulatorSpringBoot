package com.example.demo.controller;

import com.example.demo.service.BirthdayCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class MenuController {

    @Autowired
    private BirthdayCheck birthdayCheck;

    @GetMapping("/solarlab/api/menu")
    public String showMenu(Model model) {
        Map<Integer, List<String>> upcomingBirthdays = birthdayCheck.findUpcomingBirthdays();
        model.addAttribute("upcomingBirthdays", upcomingBirthdays);
        System.out.println("Upcoming birthdays: " + upcomingBirthdays);
        return "menu";
    }
}