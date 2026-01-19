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

    public List<Person> findUpcomingBirthdays() {
        List<Person> users = personService.findAll();
        LocalDate today = LocalDate.now();
        List<Person> filteredUsers = new ArrayList<>();

        for (Person user : users) {
            LocalDate birthday = user.getBirthDate();
            LocalDate thisYearBirthday = birthday.withYear(today.getYear());
            long daysUntilBirthday;

            if (!thisYearBirthday.isBefore(today)) {
                daysUntilBirthday = ChronoUnit.DAYS.between(today, thisYearBirthday);
            } else {
                daysUntilBirthday = ChronoUnit.DAYS.between(thisYearBirthday, today) * -1;
            }

            String status;
            switch ((int) daysUntilBirthday) {
                case 0: status = "Сегодня"; break;
                case 1: status = "Завтра"; break;
                case 2: status = "Послезавтра"; break;
                case -1: status = "Вчера"; break;
                case -2: status = "Позавчера"; break;
                default: continue; // Пропускаем остальные случаи
            }
            user.setBirthdayStatus(status);
            filteredUsers.add(user);
        }

        // Сортируем сначала по статусу, потом по имени
        Collections.sort(filteredUsers, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                // Порядок сортировки статусов
                String[] statuses = {"Сегодня", "Завтра", "Послезавтра", "Вчера", "Позавчера"};
                int index1 = getStatusIndex(p1.getBirthdayStatus(), statuses);
                int index2 = getStatusIndex(p2.getBirthdayStatus(), statuses);
                if (index1 != index2) {
                    return index1 - index2;
                }
                return p1.getFullName().compareTo(p2.getFullName());
            }

            private int getStatusIndex(String status, String[] statuses) {
                for (int i = 0; i < statuses.length; i++) {
                    if (statuses[i].equals(status)) {
                        return i;
                    }
                }
                return Integer.MAX_VALUE; // Если статус не найден
            }
        });

        return filteredUsers;
    }
}