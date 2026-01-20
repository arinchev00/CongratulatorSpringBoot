package com.example.demo.service;

import com.example.demo.repository.Image;
import com.example.demo.repository.ImagesRepository;
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
    private final ImagesRepository imagesRepository; // Добавляем поле

    // Конструктор с внедрением зависимостей
    public PersonService(PersonRepository personRepository, ImagesRepository imagesRepository) {
        this.personRepository = personRepository;
        this.imagesRepository = imagesRepository;
    }

    public List<Person> findAll() {
        List<Person> people = personRepository.findAll();
        Collections.sort(people, Comparator.comparing(Person::getFullName));
        return people;
    }

    public Person create(Person person, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Image image = toImageEntity(file);
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

    public void update(Long id, String fullName, LocalDate birthDate, MultipartFile photo,
                       boolean shouldRemovePhoto, Long currentPhotoId) throws IOException {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();

            // 1. Обновляем базовые поля
            person.setFullName(fullName);
            person.setBirthDate(birthDate);

            // 2. Удаляем старое фото, если нужно
            if (shouldRemovePhoto && currentPhotoId != null) {
                Image imageToRemove = imagesRepository.findById(currentPhotoId).orElse(null);
                if (imageToRemove != null) {
                    // Разрываем связь до удаления из БД
                    person.setImage(null);
                    imagesRepository.delete(imageToRemove);
                    personRepository.flush(); // Принудительная синхронизация
                    System.out.println("Фото удалено из БД, связь разорвана");
                }
            }

            // 3. Добавляем новое фото, если файл загружен
            if (photo != null && !photo.isEmpty()) {
                // Удаляем старое фото (если есть)
                if (person.getImage() != null) {
                    imagesRepository.delete(person.getImage());
                    person.setImage(null); // Явно разрываем связь
                    personRepository.flush();
                }
                Image newImage = toImageEntity(photo);
                person.addImageToPerson(newImage);
            }

            personRepository.save(person);
            System.out.println("Изменения сохранены для пользователя: " + person.getId());
        } else {
            throw new RuntimeException("Пользователь не найден");
        }
    }


    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }
}
