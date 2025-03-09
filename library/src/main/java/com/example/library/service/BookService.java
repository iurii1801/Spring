package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.*;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public BookDTO createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());

        Author author = authorRepository.findById(bookDTO.getAuthorId()).orElse(null);
        book.setAuthor(author);

        Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId()).orElse(null);
        book.setPublisher(publisher);

        List<Category> categories = categoryRepository.findAllById(bookDTO.getCategoryIds());
        book.setCategories(categories);

        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDTO.getTitle());

                    Author author = authorRepository.findById(bookDTO.getAuthorId()).orElse(null);
                    book.setAuthor(author);

                    Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId()).orElse(null);
                    book.setPublisher(publisher);

                    List<Category> categories = categoryRepository.findAllById(bookDTO.getCategoryIds());
                    book.setCategories(categories);

                    Book updatedBook = bookRepository.save(book);
                    return convertToDTO(updatedBook);
                })
                .orElse(null);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthorId(book.getAuthor().getId());
        bookDTO.setPublisherId(book.getPublisher().getId());
        bookDTO.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
        return bookDTO;
    }
}