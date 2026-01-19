package com.example.demo.service;

import com.example.demo.repository.Image;
import com.example.demo.repository.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public Person create(Person person, MultipartFile file) throws IOException {
        Image image;
        if (file.getSize() != 0){
            image = toImageEntity(file);
            person.addImageToPerson(image);
        }
        return personRepository.save(person);
    }

        private Image toImageEntity(MultipartFile file) throws IOException {
            Image image = new Image();
            image.setName(file.getName());
            image.setOriginalFileName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setSize(file.getSize());
            image.setBytes(file.getBytes());
            return image;
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