package com.maxim.controllers;


import com.maxim.model.Book;
import com.maxim.services.BooksService;
import com.maxim.services.PeopleService;
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

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String getBooks(Model model) {
        model.addAttribute("books", booksService.findAll());

        return "books/show";
    }

    @GetMapping(params = {"page", "books_per_page"})
    public String getBooks(@RequestParam(name="page") int page,
                           @RequestParam(name="books_per_page") int booksPerPage,
                           Model model) {
        model.addAttribute("books", booksService.findAll(page, booksPerPage));

        return "books/show";
    }

    @GetMapping(params = {"sort_by_year"})
    public String getBooks(@RequestParam(name="sort_by_year") boolean sortByYear, Model model) {
        model.addAttribute("books", booksService.findAll(sortByYear));

        return "books/show";
    }

    @GetMapping(params = {"page", "books_per_page", "sort_by_year"})
    public String getBooks(@RequestParam(name="page") int page,
                           @RequestParam(name="books_per_page") int booksPerPage,
                           @RequestParam(name="sort_by_year") boolean sortByYear,
                           Model model) {
        model.addAttribute("books", booksService.findAll(page, booksPerPage, sortByYear));

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
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        Optional<Book> book = booksService.findById(id);
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
        booksService.update(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable("id") int id, Model model) {
        Optional<Book> book = booksService.findById(id);
        if (book.isPresent()) {
            Book b = book.get();
            model.addAttribute("book", b);

            model.addAttribute("people", peopleService.findAll());
            if (b.getPersonId() != null) {
                peopleService.findById(b.getPersonId().getId())
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
        booksService.setPerson(book.getId(), book.getPersonId().getId());
        return "redirect:/books/" + book.getId();
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        booksService.releaseBook(id);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@ModelAttribute Book book) {
        booksService.delete(book);
        return "redirect:/books";
    }
}
