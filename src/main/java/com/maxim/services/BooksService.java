package com.maxim.services;


import com.maxim.model.Book;
import com.maxim.model.Person;
import com.maxim.repositories.BooksRepository;
import com.maxim.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            person.get().addBook(book.get());
        }
    }

    @Transactional
    public void releaseBook(int id) {
        Optional<Book> book = booksRepository.findById(id);
        book.ifPresent(value -> value.setPersonId(null));
    }
}
