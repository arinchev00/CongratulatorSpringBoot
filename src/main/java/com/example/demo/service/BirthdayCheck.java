package com.example.demo.service;

import com.example.demo.repository.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class BirthdayCheck {

    private final PersonService personService;

    @Autowired
    public BirthdayCheck(PersonService personService) {
        this.personService = personService;
    }

    public Map<Integer, List<String>> findUpcomingBirthdays() {
        List<Person> users = personService.findAll();
        LocalDate today = LocalDate.now();

        Map<Integer, List<String>> resultMap = new TreeMap<>();

        for (Person user : users) {
            LocalDate birthday = user.getBirthDate();
            LocalDate thisYearBirthday = birthday.withYear(today.getYear());

            int daysDifference = Math.toIntExact(ChronoUnit.DAYS.between(today, thisYearBirthday));

            if (resultMap.containsKey(daysDifference)) {
                resultMap.get(daysDifference).add(user.getFullName());
            } else {
                List<String> list = new ArrayList<>();
                list.add(user.getFullName());
                resultMap.put(daysDifference, list);
            }
        }

        return resultMap;
    }
}