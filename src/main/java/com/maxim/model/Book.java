package com.maxim.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private int id;
    @NotEmpty(message="Название не должно быть пустым!")
    private String title;
    @NotEmpty(message="Поле автор не должно быть пустым!")
    private String author;
    @Min(value=0, message="Год не должен быть отрицательным")
    private int year;
    private Integer personId;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }
}
