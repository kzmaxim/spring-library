package com.maxim.dao;

import com.maxim.util.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.maxim.model.Book;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", new BookMapper());
    }

    public Optional<Book> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id = ?", new Object[]{id}, new BookMapper())
                .stream()
                .findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(title, author, year, person_id) VALUES (?, ?, ?, ?)", book.getTitle(), book.getAuthor(), book.getYear(), book.getPersonId());
    }

    public void update(Book book) {
        jdbcTemplate.update("UPDATE book SET title = ?, author = ?, year = ?, person_id = ? WHERE id = ?", book.getTitle(), book.getAuthor(), book.getYear(), book.getPersonId(), book.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    public void setPerson(int bookId, int personId) {
        jdbcTemplate.update("UPDATE book SET person_id = ? WHERE id = ?", personId, bookId);
    }

    public void releaseBook(int bookId) {
        jdbcTemplate.update("UPDATE book SET person_id = NULL WHERE id = ?", bookId);
    }


}
