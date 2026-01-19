package com.example.demo.service;

import com.example.demo.repository.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        List<Person> people = personRepository.findAll();
        Collections.sort(people, Comparator.comparing(Person::getFullName));
        return people;
    }

    public Person create(Person person) {
        return personRepository.save(person);
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    public void update(Long id, String fullName, LocalDate birthDate) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            if (fullName != null) {
                person.setFullName(fullName);
            }
            if (birthDate != null) {
                person.setBirthDate(birthDate);
            }
            personRepository.save(person);
        }
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }
}