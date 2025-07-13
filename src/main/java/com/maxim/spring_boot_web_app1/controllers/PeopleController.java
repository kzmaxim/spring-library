package com.maxim.spring_boot_web_app1.controllers;


import com.maxim.spring_boot_web_app1.model.Person;
import com.maxim.spring_boot_web_app1.services.PeopleService;
import com.maxim.spring_boot_web_app1.util.PersonValidator;
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

    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showPeople(Model model) {
        model.addAttribute("people",peopleService.findAll());
        return "people/show";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        Optional<Person> person = peopleService.findById(id);
        if (person.isEmpty()) {
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
    public String addPerson(@ModelAttribute @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        Optional<Person> person = peopleService.findById(id);
        if (person.isEmpty()) {
            return "redirect:/people";
        }
        model.addAttribute("person", person.get());
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String editPerson(@ModelAttribute @Valid Person person, BindingResult bindingResult, Model model) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("person", person);
            return "people/edit";
        }
        peopleService.update(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@ModelAttribute Person person) {
        peopleService.delete(person);
        return "redirect:/people";
    }
}
