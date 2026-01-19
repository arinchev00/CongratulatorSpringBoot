package com.example.demo.controller;

import com.example.demo.repository.Person;
import com.example.demo.service.BirthdayCheck;
import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PersonController {


    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/solarlab/api/menu/all")
    public String allPerson(Model model) {
        List<Person> people = personService.findAll();
        model.addAttribute("people", people);
        return "allPerson"; // Возвращаем имя HTML-шаблона для отображения всех пользователей
    }

    @GetMapping("/solarlab/api/menu/create")
    public ModelAndView createPage() {
        return new ModelAndView("createPerson"); // Здесь "create" - название шаблона Thymeleaf
    }

    @PostMapping(path = "solarlab/api/menu/create")
    public String createPerson(@RequestParam String fullName, @RequestParam LocalDate birthDate) {
        Person newPerson = new Person(null, fullName, birthDate);
        personService.create(newPerson);
        return "redirect:/solarlab/api/menu"; // Перенаправляем на страницу меню
    }

    @PostMapping(path = "/solarlab/api/menu/edit")
    @ResponseBody
    public Map<String, Object> editPerson(
            @RequestParam Long id,
            @RequestParam String fullName,
            @RequestParam LocalDate birthDate
    ) {
        personService.update(id, fullName, birthDate);
        return Map.of("status", "success", "message", "Данные пользователя изменены!");
    }

    @DeleteMapping(path = "solarlab/api/menu/delete/{id}")
    @ResponseBody
    public Map<String, String> deletePerson(@PathVariable Long id) {
            personService.delete(id);
            return Map.of("status", "success", "message", "Пользователь удалён");
        }
    }
