package com.maxim.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


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
}
