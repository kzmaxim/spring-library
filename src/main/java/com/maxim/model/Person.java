package com.maxim.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Person {
    private int id;
    @NotEmpty(message="Имя не должно быть пустым")
    private String name;
    @Min(value=1900, message = "Год рождения должен быть больше 1900!")
    private int year;
    private List<Book> books;

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }
}
