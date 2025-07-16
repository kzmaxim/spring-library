package com.maxim.spring_boot_web_app1.integration.services;


import com.maxim.spring_boot_web_app1.model.Book;
import com.maxim.spring_boot_web_app1.model.Person;
import com.maxim.spring_boot_web_app1.services.BooksService;
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
public class BooksServiceIntegrationTest {

    @Autowired
    private BooksService booksService;
    @Autowired
    private PeopleService peopleService;

    @Test
    void findAll() {
        List<Book> actualResult = booksService.findAll();

        assertFalse(actualResult.isEmpty());
    }

    @Test
    void findById() {
        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPersonId(new Person());
        Optional<Book> expectedBook = Optional.of(book);
        booksService.save(expectedBook.get());

        Optional<Book> actualBook = booksService.findById(expectedBook.get().getId());

        assertTrue(actualBook.isPresent());

        assertEquals(expectedBook.get(), actualBook.get());
    }

    @Test
    void delete() {
        Optional<Book> book = booksService.findById(1);
        assertTrue(book.isPresent());

        booksService.delete(book.get());

        assertFalse(booksService.findById(1).isPresent());
    }

    @Test
    void setPerson() {
        int bookId = 1;
        int personId = 2;
        Optional<Book> book = booksService.findById(bookId);
        Optional<Person> person = peopleService.findById(personId);

        assertTrue(book.isPresent());
        assertTrue(person.isPresent());

        booksService.setPerson(bookId, personId);

        assertNotNull(book.get().getAddedBookAt());

        assertTrue(person.get().getBooks().contains(book.get()));
    }

    @Test
    void releaseBook() {
        Optional<Book> book = booksService.findById(1);
        assertTrue(book.isPresent());

        booksService.releaseBook(1);

        assertNull(book.get().getAddedBookAt());
        assertNull(book.get().getPersonId());
    }

    @Test
    void findByNameStartingWith() {
        Book book = new Book();
        book.setTitle("Test");
        book.setAuthor("Author");
        book.setYear(2000);
        List<Book> expectedResult = List.of(book);

        booksService.save(book);

        List<Book> actualResult = booksService.findByNameStartingWith("Test");

        assertFalse(actualResult.isEmpty());
        assertEquals(expectedResult, actualResult);
    }
}
