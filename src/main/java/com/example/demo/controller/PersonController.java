package com.example.demo.controller;

import com.example.demo.repository.Person;
import com.example.demo.service.BirthdayCheck;
import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
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
        model.addAttribute("people", people != null ? people : Collections.emptyList());
        return "allPerson";
    }

    @GetMapping("/solarlab/api/menu/create")
    public ModelAndView createPage() {
        return new ModelAndView("createPerson"); // Здесь "create" - название шаблона Thymeleaf
    }

    @PostMapping("/solarlab/api/menu/create")
    public String createPerson(
            @RequestParam("fullName") String fullName,
            @RequestParam LocalDate birthDate,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Model model
    ) {
        Person person = new Person();
        person.setFullName(fullName);
        person.setBirthDate(birthDate);

        try {
            personService.create(person, file); // сохранение с изображением
        } catch (IOException e) {
            model.addAttribute("error", "Ошибка при загрузке файла: " + e.getMessage());
            return "menu/create";
        }
        return "redirect:/solarlab/api/menu";
    }


    @PostMapping("/solarlab/api/menu/edit")
    @ResponseBody
    public Map<String, Object> editPerson(
            @RequestParam Long id,
            @RequestParam String fullName,
            @RequestParam LocalDate birthDate,
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(value = "removePhoto", required = false) String removePhotoFlag,
            @RequestParam(value = "currentPhotoId", required = false) Long currentPhotoId
    ) {
        try {
            personService.update(id, fullName, birthDate, photo, "true".equals(removePhotoFlag), currentPhotoId);
            return Map.of("status", "success", "message", "Данные сохранены!");
        } catch (Exception e) {
            return Map.of("status", "error", "message", e.getMessage());
        }
    }

    @DeleteMapping(path = "solarlab/api/menu/delete/{id}")
    @ResponseBody
    public Map<String, String> deletePerson(@PathVariable Long id) {
            personService.delete(id);
            return Map.of("status", "success", "message", "Пользователь удалён");
        }
    }
