package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.BookDto;
import com.example.librarymanagement.entity.*;
import com.example.librarymanagement.repository.*;
import com.example.librarymanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto createBook(BookDto dto) {
        Book book = mapToEntity(dto);
        return mapToDto(bookRepository.save(book));
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return mapToDto(book);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto updateBook(Long id, BookDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(dto.getTitle());
        book.setAuthor(getAuthor(dto.getAuthorId()));
        book.setPublisher(getPublisher(dto.getPublisherId()));
        book.setCategories(getCategories(dto.getCategoryIds()));

        return mapToDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // --------- Маппинг ---------

    private Book mapToEntity(BookDto dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(getAuthor(dto.getAuthorId()))
                .publisher(getPublisher(dto.getPublisherId()))
                .categories(getCategories(dto.getCategoryIds()))
                .build();
    }

    private BookDto mapToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .publisherId(book.getPublisher().getId())
                .categoryIds(book.getCategories().stream().map(Category::getId).collect(Collectors.toList()))
                .build();
    }

    private Author getAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    private Publisher getPublisher(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
    }

    private List<Category> getCategories(List<Long> ids) {
        return categoryRepository.findAllById(ids);
    }
}
