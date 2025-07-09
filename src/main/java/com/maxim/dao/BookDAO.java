package com.maxim.dao;

import com.maxim.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.maxim.model.Book;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public BookDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Book", Book.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<Book> findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        if (book != null) {
            return Optional.of(book);
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public void save(Book book) {
        sessionFactory.getCurrentSession().persist(book);
    }

    @Transactional
    public void update(Book book) {
        Session session = sessionFactory.getCurrentSession();
        Book updateBook = session.get(Book.class, book.getId());
        if (updateBook != null) {
            updateBook.setTitle(book.getTitle());
            updateBook.setAuthor(book.getAuthor());
            updateBook.setYear(book.getYear());
        }
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        if (book != null) {
            session.remove(book);
        }
    }

    @Transactional
    public void setPerson(int bookId, int personId) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, personId);
        Book book = session.get(Book.class, bookId);
        if (person != null && book != null) {
            book.setPersonId(person);
            person.getBooks().add(book);
        }
    }

    @Transactional
    public void releaseBook(int bookId) {
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, bookId);
        if (book != null) {
            book.setPersonId(null);
        }
    }


}
