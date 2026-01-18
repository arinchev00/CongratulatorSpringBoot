package com.example.demo.controller;

import com.example.demo.repository.Person;
import com.example.demo.service.BirthdayCheck;
import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class PersonController {


    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(path = "solarlab/api/menu/all")
    public String allPerson(Model model) {
        List<Person> people = personService.findAll();
        model.addAttribute("people", people);
        return "allPerson"; // Возвращаем имя HTML-шаблона для отображения всех пользователей
    }

    @GetMapping(path = "solarlab/api/menu/create")
    public String createPersonForm() {
        return "createPerson"; // Возвращаем имя HTML-шаблона для создания нового пользователя
    }

    @PostMapping(path = "solarlab/api/menu/create")
    public String createPerson(@RequestParam String fullName, @RequestParam LocalDate birthDate) {
        Person newPerson = new Person(null, fullName, birthDate);
        personService.create(newPerson);
        return "redirect:/solarlab/api/menu"; // Перенаправляем на страницу меню
    }

    @GetMapping(path = "solarlab/api/menu/edit")
    public String editPersonForm(@RequestParam Long id, Model model) {
        Optional<Person> optionalPerson = personService.findById(id);
        if (optionalPerson.isPresent()) {
            model.addAttribute("person", optionalPerson.get());
            return "editPerson"; // Возвращаем имя HTML-шаблона для редактирования пользователя
        } else {
            return "redirect:/solarlab/api/menu"; // Перенаправляем на страницу меню, если пользователь не найден
        }
    }

    @PostMapping(path = "solarlab/api/menu/edit")
    public String editPerson(@RequestParam Long id, @RequestParam String fullName, @RequestParam LocalDate birthDate) {
        personService.update(id, fullName, birthDate);
        return "redirect:/solarlab/api/menu"; // Перенаправляем на страницу меню
    }

    @GetMapping(path = "solarlab/api/menu/delete")
    public String deletePersonForm(@RequestParam Long id, Model model) {
        Optional<Person> optionalPerson = personService.findById(id);
        if (optionalPerson.isPresent()) {
            model.addAttribute("person", optionalPerson.get());
            return "deletePerson"; // Возвращаем имя HTML-шаблона для удаления пользователя
        } else {
            return "redirect:/solarlab/api/menu"; // Перенаправляем на страницу меню, если пользователь не найден
        }
    }

    @PostMapping(path = "solarlab/api/menu/delete")
    public String deletePerson(@RequestParam Long id) {
        personService.delete(id);
        return "redirect:/solarlab/api/menu"; // Перенаправляем на страницу меню
    }

    @GetMapping(path = "solarlab/api/menu/exit")
    public String exit() {
        System.out.println("Приложение закрыто.");
        System.exit(0);
        return "redirect:/solarlab/api/menu"; // Перенаправляем на страницу меню
    }
}