package com.maxim.util;

import com.maxim.dao.PersonDAO;
import com.maxim.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        Optional<Person> personFromBD = personDAO.findByName(person.getName());

        if (personFromBD.isPresent() && personFromBD.get().getId() != person.getId()) {
            errors.rejectValue("name", "", "Человек с таким именем уже существует");
        }
    }
}
