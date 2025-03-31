package com.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // Книга имеет одного автора
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    // Книга имеет одного издателя
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    // Книга может принадлежать к нескольким категориям
    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}
