package com.example.demo.controller;

import com.example.demo.repository.Person;
import com.example.demo.service.BirthdayCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MenuController {

    @Autowired
    private BirthdayCheck birthdayCheck;

    @GetMapping("/api/menu")
    public String showMenu(Model model) {
        List<Person> upcomingBirthdays = birthdayCheck.findUpcomingBirthdays();
        model.addAttribute("upcomingBirthdays", upcomingBirthdays);
        return "menu";
    }
}