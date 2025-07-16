package com.maxim.spring_boot_web_app1.services;

import com.maxim.spring_boot_web_app1.model.Book;
import com.maxim.spring_boot_web_app1.model.Person;
import com.maxim.spring_boot_web_app1.repositories.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleServiceTest {

    @Mock
    private PeopleRepository peopleRepository;

    @InjectMocks
    private PeopleService peopleService;

    @Test
    void findAll() {
        doReturn(new ArrayList<>()).when(peopleRepository).findAll();
        List<Person> actualResult = peopleService.findAll();

        assertTrue(actualResult.isEmpty());

        assertEquals(new ArrayList<>(), actualResult);

        verify(peopleRepository).findAll();
        verifyNoMoreInteractions(peopleRepository);
    }

    @Test
    void findById() {
        int personId = 1;
        Book overdueBook = new Book();
        overdueBook.setAddedBookAt(LocalDate.now().minusDays(11));
        overdueBook.setOverdue(false);

        Book notOverdueBook = new Book();
        notOverdueBook.setAddedBookAt(LocalDate.now().minusDays(5));
        notOverdueBook.setOverdue(false);

        Person person = new Person();
        person.setId(personId);
        person.setBooks(List.of(overdueBook, notOverdueBook));

        doReturn(Optional.of(person)).when(peopleRepository).findById(personId);

        Optional<Person> result = peopleService.findById(personId);

        assertTrue(result.isPresent());

        List<Book> books = result.get().getBooks();
        assertTrue(books.get(0).isOverdue());
        assertFalse(books.get(1).isOverdue());

        verify(peopleRepository).findById(personId);
        verifyNoMoreInteractions(peopleRepository);
    }

    @Test
    void findByName() {
        Person person = new Person();
        person.setName("Test");

        doReturn(Optional.of(person)).when(peopleRepository).findByName(person.getName());

        Optional<Person> result = peopleService.findByName(person.getName());

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());

        verify(peopleRepository).findByName(person.getName());
        verifyNoMoreInteractions(peopleRepository);
    }

    @Test
    void save() {
        Person person = new Person();

        peopleService.save(person);

        verify(peopleRepository).save(person);
        verifyNoMoreInteractions(peopleRepository);
    }

    @Test
    void delete() {
        Person person = new Person();
        person.setId(1);

        peopleService.delete(person);

        assertTrue(peopleRepository.findById(1).isEmpty());

        verify(peopleRepository).delete(person);
    }
}