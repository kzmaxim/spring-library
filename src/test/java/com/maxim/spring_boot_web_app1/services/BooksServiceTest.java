package com.maxim.spring_boot_web_app1.services;

import com.maxim.spring_boot_web_app1.model.Book;
import com.maxim.spring_boot_web_app1.model.Person;
import com.maxim.spring_boot_web_app1.repositories.BooksRepository;
import com.maxim.spring_boot_web_app1.repositories.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

    @Mock
    private BooksRepository booksRepository;
    @Mock
    private PeopleRepository peopleRepository;

    @InjectMocks
    private BooksService booksService;

    @Test
    void findAll() {
        doReturn(new ArrayList<Book>()).when(booksRepository).findAll();
        List<Book> actualResult = booksService.findAll();

        assertTrue(actualResult.isEmpty());

        assertEquals(new ArrayList<Book>(), actualResult);

        verify(booksRepository).findAll();
    }

    @Test
    void findById() {
        Optional<Book> expectedBook = Optional.of(new Book());
        expectedBook.get().setId(1);

        doReturn(expectedBook).when(booksRepository).findById(1);

        Optional<Book> actualBook = booksService.findById(1);

        assertTrue(actualBook.isPresent());

        assertEquals(expectedBook.get(), actualBook.get());

        verify(booksRepository).findById(1);
    }

    @Test
    void save() {
        Book book = new Book();

        booksService.save(book);

        verify(booksRepository).save(book);
    }

    @Test
    void delete() {
        Book book = new Book();
        booksService.delete(book);
        verify(booksRepository).delete(book);
    }

    @Test
    void setPerson() {
        int bookId = 1;
        int personId = 2;

        Book book = new Book();
        book.setId(bookId);

        Person person = new Person();
        person.setId(personId);
        person.setBooks(new ArrayList<>());

        doReturn(Optional.of(book)).when(booksRepository).findById(bookId);
        doReturn(Optional.of(person)).when(peopleRepository).findById(personId);

        booksService.setPerson(bookId, personId);

        assertNotNull(book.getAddedBookAt());

        assertTrue(person.getBooks().contains(book));

        verify(booksRepository).findById(bookId);
        verify(peopleRepository).findById(personId);
        verifyNoMoreInteractions(booksRepository, peopleRepository);
    }


    @Test
    void releaseBook() {
        Book book = new Book();
        book.setId(1);
        book.setPersonId(new Person());
        book.setAddedBookAt(LocalDate.now());

        doReturn(Optional.of(book)).when(booksRepository).findById(1);

        booksService.releaseBook(1);

        assertNull(book.getAddedBookAt());
        assertNull(book.getPersonId());

        verify(booksRepository).findById(1);
    }

    @Test
    void findByNameStartingWith() {
        Book book = new Book();
        book.setTitle("Test");
        List<Book> expectedResult = List.of(book);

        doReturn(expectedResult).when(booksRepository).findByTitleStartingWith("Test");

        List<Book> actualResult = booksService.findByNameStartingWith("Test");

        assertFalse(actualResult.isEmpty());
        assertEquals(expectedResult, actualResult);

        verify(booksRepository).findByTitleStartingWith("Test");
        verifyNoMoreInteractions(booksRepository);
    }
}