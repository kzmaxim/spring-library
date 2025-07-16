package com.maxim.spring_boot_web_app1.integration.services;


import com.maxim.spring_boot_web_app1.model.Book;
import com.maxim.spring_boot_web_app1.model.Person;
import com.maxim.spring_boot_web_app1.services.PeopleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PeopleServiceIntegrationTest {

    @Autowired
    private PeopleService peopleService;

    @Test
    void findAll() {
        List<Person> actualResult = peopleService.findAll();

        assertFalse(actualResult.isEmpty());
    }

    @Test
    void findById() {
        int personId = 3;

        Optional<Person> result = peopleService.findById(personId);

        assertTrue(result.isPresent());

        List<Book> books = result.get().getBooks();
        books.get(0).setOverdue(true);

        assertTrue(books.get(0).isOverdue());
    }

    @Test
    void findByName() {
        Person person = new Person();
        person.setName("Test");
        person.setYear(2000);

        peopleService.save(person);

        Optional<Person> result = peopleService.findByName("Test");

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
    }

    @Test
    void delete() {
        int personId = 3;
        Optional<Person> result = peopleService.findById(personId);
        assertTrue(result.isPresent());

        peopleService.delete(result.get());

        assertFalse(peopleService.findById(personId).isPresent());
    }
}
