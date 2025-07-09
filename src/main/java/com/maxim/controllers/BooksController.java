package com.maxim.controllers;


import com.maxim.dao.BookDAO;
import com.maxim.dao.PersonDAO;
import com.maxim.model.Book;
import com.maxim.model.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String getBooks(Model model) {
        model.addAttribute("books", bookDAO.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model, @ModelAttribute Book book) {
        model.addAttribute("book", book);

        return "books/new";
    }

    @PostMapping
    public String saveBook(@ModelAttribute @Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "books/new";
        }
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        Optional<Book> book = bookDAO.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "books/edit";
        }
        return "redirect:/books";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute @Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "books/edit";
        }
        bookDAO.update(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable("id") int id, Model model) {
        Optional<Book> book = bookDAO.findById(id);
        if (book.isPresent()) {
            Book b = book.get();
            model.addAttribute("book", b);

            model.addAttribute("people", personDAO.findAll());
            if (b.getPersonId() != null) {
                personDAO.findById(b.getPersonId().getId())
                        .ifPresent(owner -> model.addAttribute("owner", owner));
            } else {
                model.addAttribute("owner", null);
            }

            return "books/book";
        }

        return "redirect:/books";
    }


    @PatchMapping("/setPerson")
    public String setPerson(@ModelAttribute Book book) {
        bookDAO.setPerson(book.getId(), book.getPersonId().getId());
        return "redirect:/books/" + book.getId();
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookDAO.releaseBook(id);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@ModelAttribute Book book) {
        bookDAO.delete(book.getId());
        return "redirect:/books";
    }


}
