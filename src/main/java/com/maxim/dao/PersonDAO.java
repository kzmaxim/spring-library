package com.maxim.dao;

import com.maxim.model.Person;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> findAll() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("from Person", Person.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<Person> findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        if (person != null) {
            Hibernate.initialize(person.getBooks());
            return Optional.of(person);
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<Person> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        List<Person> people = session.createQuery("from Person where name = :name", Person.class).setParameter("name", name).getResultList();

        return people.stream().findFirst();
    }

    @Transactional
    public void save(Person person) {
        sessionFactory.getCurrentSession().persist(person);
    }

    @Transactional
    public void update(Person person) {
        Session session = sessionFactory.getCurrentSession();
        Person updatePerson = session.get(Person.class, person.getId());
        if (updatePerson != null) {
            updatePerson.setName(person.getName());
            updatePerson.setYear(person.getYear());
            updatePerson.setBooks(person.getBooks());
        }
    }

    @Transactional
    public void delete(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(person);
    }
}
