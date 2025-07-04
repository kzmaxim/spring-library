package com.maxim.controllers;


import com.maxim.dao.PersonDAO;
import com.maxim.model.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String showPeople(Model model) {
        model.addAttribute("people", personDAO.findAll());
        return "people/show";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        Optional<Person> person = personDAO.findById(id);
        if (!person.isPresent()) {
            return "redirect:/people";
        }
        model.addAttribute("person", person.get());

        return "people/person";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute Person person, Model model) {
        model.addAttribute("person", person);

        return "people/new";
    }

    @PostMapping
    public String addPerson(@ModelAttribute @Valid Person person, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        Optional<Person> person = personDAO.findById(id);
        if (!person.isPresent()) {
            return "redirect:/people";
        }
        model.addAttribute("person", person.get());
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String editPerson(@ModelAttribute @Valid Person person, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("person", person);
            return "people/edit";
        }
        personDAO.update(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@ModelAttribute Person person, Model model) {
        personDAO.delete(person);
        return "redirect:/people";
    }
}
