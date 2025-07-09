package com.maxim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message="Название не должно быть пустым!")
    private String title;

    @NotEmpty(message="Поле автор не должно быть пустым!")
    private String author;

    @Min(value=0, message="Год не должен быть отрицательным")
    private int year;

    @ManyToOne
    @JoinColumn(name="person_id", referencedColumnName = "id")
    private Person personId;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }
}
