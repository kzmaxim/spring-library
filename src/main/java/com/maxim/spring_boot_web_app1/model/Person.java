package com.maxim.spring_boot_web_app1.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message="Имя не должно быть пустым")
    private String name;

    @Min(value=1900, message = "Год рождения должен быть больше 1900!")
    private int year;

    @OneToMany(mappedBy = "personId", cascade = CascadeType.PERSIST)
    private List<Book> books;

    public void addBook(Book book) {
        books.add(book);
        book.setPersonId(this);
    }


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year=" + year +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return year == person.year && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year);
    }
}
