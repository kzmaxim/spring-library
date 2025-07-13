package com.maxim.spring_boot_web_app1.services;


import com.maxim.spring_boot_web_app1.model.Book;
import com.maxim.spring_boot_web_app1.model.Person;
import com.maxim.spring_boot_web_app1.repositories.BooksRepository;
import com.maxim.spring_boot_web_app1.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    public List<Book> findAll(int page, int booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        }
        else {
            return findAll(page, booksPerPage);
        }
    }

    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void delete(Book book) {
        booksRepository.delete(book);
    }

    @Transactional
    public void setPerson(int id, int personId) {
        Optional<Book> book = booksRepository.findById(id);
        Optional<Person> person = peopleRepository.findById(personId);
        if (book.isPresent() && person.isPresent()) {
            book.get().setAddedBookAt(LocalDate.now());
            person.get().addBook(book.get());
        }
    }

    @Transactional
    public void releaseBook(int id) {
        Optional<Book> book = booksRepository.findById(id);
        book.ifPresent(value -> {
            value.setPersonId(null);
            value.setAddedBookAt(null);
        });
    }

    public List<Book> findByNameStartingWith(String name) {
        return booksRepository.findByTitleStartingWith(name);
    }
}
