package com.maxim.repositories;


import com.maxim.model.Person;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByName(@NotEmpty(message="Имя не должно быть пустым") String name);
}
