package com.maxim.services;


import com.maxim.model.Book;
import com.maxim.model.Person;
import com.maxim.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        person.ifPresent(p -> {
            Hibernate.initialize(p.getBooks());
            for (Book book : p.getBooks()) {
                book.setOverdue(book.getAddedBookAt() != null && book.getAddedBookAt().plusDays(10).isBefore(LocalDate.now()));
            }
        });
        return person;
    }


    public Optional<Person> findByName(String name) {
        return peopleRepository.findByName(name);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(Person person) {
        peopleRepository.delete(person);
    }
}
