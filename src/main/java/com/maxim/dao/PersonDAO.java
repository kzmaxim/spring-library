package com.maxim.dao;

import com.maxim.model.Book;
import com.maxim.model.Person;
import com.maxim.util.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> findAll() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> findById(Integer id) {
        Optional<Person> people = jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream()
                .findFirst();

        if (people.isEmpty()) {
            return Optional.empty();
        }

        Person person = people.get();

        List<Book> books = jdbcTemplate.query("SELECT * FROM book WHERE person_id = ?", new Object[]{person.getId()}, new BookMapper());

        person.setBooks(books);

        return Optional.of(person);
    }
    public Optional<Person> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM person WHERE name = ?", new Object[]{name}, new BeanPropertyRowMapper<>(Person.class))
                .stream()
                .findFirst();
    }
    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, year) VALUES (?, ?)", person.getName(), person.getYear());
    }
    public void update(Person person) {
        jdbcTemplate.update("UPDATE person SET name = ?, year = ? WHERE id = ?", person.getName(), person.getYear(), person.getId());
    }
    public void delete(Person person) {
        jdbcTemplate.update("DELETE FROM person WHERE id = ?", person.getId());
    }
}
